package ru.practicum.ewm.public_api.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.admin_api.comments.CommentsService;
import ru.practicum.ewm.admin_api.comments.model.CommentDto;
import ru.practicum.ewm.admin_api.comments.model.SortByDate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentsControllerPublic {

    private final CommentsService commentsService;

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getAllByEventId(@PathVariable Long eventId,
                                            @RequestParam(required = false) SortByDate sort,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get comments by EventId {} sort {} from {} size {}", eventId, sort, from, size);
        return commentsService.getAllByEventId(eventId, sort, from, size);
    }
}
