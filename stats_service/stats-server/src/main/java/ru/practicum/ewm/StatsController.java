package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.FORMATTER;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHit create(@Validated @RequestBody EndpointHit endpointHit) {
        log.info("Create endpointHit {} {}", endpointHit.getUri(), endpointHit.getIp());
        return statsService.create(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getAll(@RequestParam String start, /*@DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime start,*/
                                  @RequestParam String end, /*@DateTimeFormat(pattern = DATA_PATTERN) LocalDateTime end,*/
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false", required = false) boolean unique) {
        log.info("Get all stats {} - {} _unique: {} _uris: {} ", LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER), unique, uris);
        return statsService.getAll(LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER), uris, unique);

        /*log.info("Get all stats {} - {} _unique: {} _uris: {} ", start, end, unique, uris);
        return statsService.getAll(start, end, uris, unique);*/
    }


}
