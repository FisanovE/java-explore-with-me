package ru.practicum.ewm.private_api.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.comments.CommentsService;
import ru.practicum.ewm.admin_api.comments.model.CommentDto;
import ru.practicum.ewm.admin_api.comments.model.CommentNew;

import javax.validation.Valid;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/comments")
public class CommentsControllerPrivate {

    private final CommentsService commentsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentDto create(@PathVariable long userId,
                             @RequestParam long eventId,
                             @RequestBody @Valid CommentNew commentNew) {
        log.info("Create comment user {} event {}", userId, eventId);
        return commentsService.create(userId, eventId, commentNew);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable long userId,
                             @PathVariable long commentId,
                             @RequestBody @Valid CommentNew commentNew) {
        log.info("Update comment {} user {}", commentId, userId);
        return commentsService.update(userId, commentId, commentNew);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long userId,
                       @PathVariable long commentId) {
        log.info("Delete comment {} user {}", commentId, userId);
        commentsService.delete(userId, commentId);
    }
}
