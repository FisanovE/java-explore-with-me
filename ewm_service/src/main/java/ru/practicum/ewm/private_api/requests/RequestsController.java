package ru.practicum.ewm.private_api.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.private_api.requests.model.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestsController {

    private final RequestsService requestsService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable long userId,
                                                          @RequestParam long eventId) {
        log.info("Create request users {} event {}", userId, eventId);
        return new ResponseEntity<>(requestsService.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateCancel(@PathVariable long userId,
                                          @PathVariable long requestId) {
        log.info("Update users {} request {}", userId, requestId);
        return new ResponseEntity<>(requestsService.updateCancel(userId, requestId), HttpStatus.OK);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllById(@PathVariable long userId) {
        log.info("Get all requests by user {}", userId);
        return requestsService.getAllByUserId(userId);
    }
}
