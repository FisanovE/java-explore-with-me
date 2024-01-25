package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.exeptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit create(EndpointHit endpointHit) {
        return statsRepository.save(endpointHit);
    }

    public List<ViewStats> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new ValidationException("RangeEnd not can be before rangeStart. RangeStart: " + start + " rangeEnd: " + end);
        }
        if (uris == null) {
            if (unique) return statsRepository.findAllByDistinctIp(start, end);
            return statsRepository.findAll(start, end);
        } else {
            if (unique)  return statsRepository.findAllByUriByDistinctIp(start, end, uris);
            return statsRepository.findAllByUri(start, end, uris);
        }
    }
}
