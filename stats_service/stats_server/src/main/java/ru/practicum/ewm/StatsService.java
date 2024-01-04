package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public EndpointHit create(EndpointHit endpointHit) {
        System.out.println("Create endpointHit2");
        return statsRepository.save(endpointHit);
    }

    public List<ViewStats> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null) {
            if (unique) return statsRepository.findAllByDistinctIp(start, end);
            return statsRepository.findAll(start, end);
        } else {
            if (unique)  return statsRepository.findAllByUriByDistinctIp(start, end, uris);
            return statsRepository.findAllByUri(start, end, uris);
        }
    }
}
