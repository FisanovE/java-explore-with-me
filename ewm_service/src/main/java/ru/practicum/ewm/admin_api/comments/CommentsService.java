package ru.practicum.ewm.admin_api.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.admin_api.comments.model.Comment;
import ru.practicum.ewm.admin_api.comments.model.CommentDto;
import ru.practicum.ewm.admin_api.comments.model.CommentNew;
import ru.practicum.ewm.admin_api.comments.model.SortByDate;
import ru.practicum.ewm.admin_api.comments.model.SortCommentsAdmin;
import ru.practicum.ewm.admin_api.events.EventsRepository;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.admin_api.users.UserRepository;
import ru.practicum.ewm.admin_api.users.model.User;
import ru.practicum.ewm.exeptions.ConflictDataException;
import ru.practicum.ewm.exeptions.NotFoundException;
import ru.practicum.ewm.exeptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.Constants.FORMATTER;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    public CommentDto create(long userId, long eventId, CommentNew commentNew) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictDataException("Only published events state can be  commented. Event: " + event.getId() +
                    " has state: " + event.getState());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Comment comment = commentsMapper.toComment(commentNew);
        comment.setUser(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        return commentsMapper.toCommentDto(commentsRepository.save(comment));
    }

    public CommentDto update(long userId, long commentId, CommentNew commentNew) {
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));
        if (comment.getUser().getId() != userId) throw new ConflictDataException("User ID: " + userId
                + " not equal to ID of author comment: " + comment.getUser().getId());
        comment.setText(commentNew.getText());
        comment.setLastUpdated(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        return commentsMapper.toCommentDto(commentsRepository.save(comment));
    }

    public CommentDto updateAdmin(long commentId, CommentNew commentNew) {
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));
        comment.setText(commentNew.getText());
        comment.setLastUpdated(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        return commentsMapper.toCommentDto(commentsRepository.save(comment));
    }

    public void delete(@PathVariable long userId, @PathVariable long commentId) {
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));
        if (comment.getUser().getId() != userId) throw new ConflictDataException("User ID: " + userId
                + " not equal to ID of author comment: " + comment.getUser().getId());
        commentsRepository.deleteById(commentId);
    }

    public void deleteAdmin(@PathVariable long commentId) {
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));
        commentsRepository.deleteById(commentId);
    }

    public List<CommentDto> getAllByFilters(String text, List<Long> users, List<Long> events, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, SortCommentsAdmin sort, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("RangeEnd not can be before rangeStart. RangeStart: " + rangeStart + " rangeEnd: " + rangeEnd);
        }
//        if (rangeStart == null) rangeStart = LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER);

        if (sort != null && sort.equals(SortCommentsAdmin.EVENT_ID)) {
            pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "event.id"));
        } else if (sort != null && sort.equals(SortCommentsAdmin.USER_ID)) {
            pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "user.id"));
        }
        List<Comment> comments = commentsRepository.findAllByFilters(text, users, events, rangeStart, rangeEnd, pageRequest);

        return comments.stream().map(commentsMapper::toCommentDto).collect(Collectors.toList());
    }

    public List<CommentDto> getAllByEventId(Long eventId, SortByDate sort, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "created"));

        if (sort != null && sort.equals(SortByDate.DESC_SORT)) {
            pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        }
        List<Comment> comments = commentsRepository.findAllByEventId(eventId, pageRequest);

        return comments.stream().map(commentsMapper::toCommentDto).collect(Collectors.toList());
    }
}
