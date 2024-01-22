package ru.practicum.ewm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.Constants.FORMATTER;

@Slf4j
@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(EndpointHit endpointHit) {
        log.info("Client send EndpointHit: {} {}", endpointHit.getUri(), endpointHit.getIp());
        return post("/hit", endpointHit);
    }

    public ResponseEntity<Object> getAll(LocalDateTime start, LocalDateTime end, @Nullable List<String> uris, boolean unique) {
        log.info("Client getAll start: {} end: {}", start, end);
        Map<String, Object> parameters;
        if (uris == null) {
            parameters = Map.of("start", URLEncoder.encode(start.format(FORMATTER), StandardCharsets.UTF_8),
                    "end", URLEncoder.encode(end.format(FORMATTER), StandardCharsets.UTF_8),
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
        parameters = Map.of("start", URLEncoder.encode(start.format(FORMATTER), StandardCharsets.UTF_8),
                "end", URLEncoder.encode(end.format(FORMATTER), StandardCharsets.UTF_8),
                "uris", String.join(",", uris),
                "unique", unique);
        return get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", parameters);
    }

}
