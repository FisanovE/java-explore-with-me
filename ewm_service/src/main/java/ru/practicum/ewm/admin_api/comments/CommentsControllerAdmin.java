package ru.practicum.ewm.admin_api.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.comments.model.CommentDto;
import ru.practicum.ewm.admin_api.comments.model.CommentNew;
import ru.practicum.ewm.admin_api.comments.model.SortCommentsAdmin;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
public class CommentsControllerAdmin {

    private final CommentsService commentsService;

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable long commentId,
                             @RequestBody @Valid CommentNew commentNew) {
        log.info("Update admin comment {}", commentId);
        return commentsService.updateAdmin(commentId, commentNew);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long commentId) {
        log.info("Delete comment admin event {}", commentId);
        commentsService.deleteAdmin(commentId);
    }

    @GetMapping
    public List<CommentDto> getAllByFilter(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) List<Long> users,
                                           @RequestParam(required = false) List<Long> events,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(required = false) SortCommentsAdmin sort,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get comments by Filter text {} users {} events {} start {} end {} sort {} from {} size {}", text,
                users, events, rangeStart, rangeEnd, sort, from, size);
        return commentsService.getAllByFilters(text, users, events, rangeStart, rangeEnd, sort, from, size);
    }
}
