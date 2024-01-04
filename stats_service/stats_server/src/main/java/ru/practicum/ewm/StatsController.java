package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit create(@Validated @RequestBody EndpointHit endpointHit) {
        log.info("Create endpointHit");
        return statsService.create(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getAll(@RequestParam @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false", required = false) boolean unique) {
        log.info("Get all stats {} - {} _unique: {} _uris: {} ", start, end, unique, uris);
        return statsService.getAll(start, end, uris, unique);
    }


}
