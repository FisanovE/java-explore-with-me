package ru.practicum.ewm.admin_api.comments;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.admin_api.comments.model.Comment;
import ru.practicum.ewm.admin_api.comments.model.CommentDto;
import ru.practicum.ewm.admin_api.comments.model.CommentNew;

@Component
public class CommentsMapper {
    public Comment toComment(CommentNew commentNew) {
        Comment comment = new Comment();
        comment.setText(commentNew.getText());
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setCreated(comment.getCreated());
        if (comment.getLastUpdated() != null) commentDto.setLastUpdated(comment.getLastUpdated());
        return commentDto;
    }
}
