package ru.practicum.ewm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("SELECT new ru.practicum.ewm.ViewStats(e.app, e.uri, CAST(COUNT(e.ip) AS int) AS hits) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAll(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(e.app, e.uri, CAST(COUNT(e.ip) AS int) AS hits) " +
            "FROM EndpointHit e " +
            "WHERE e.uri IN :uris AND e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAllByUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.ViewStats(e.app, e.uri, CAST(COUNT(distinct e.ip) AS int) AS hits) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAllByDistinctIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.ViewStats(e.app, e.uri, CAST(COUNT(distinct e.ip) AS int) AS hits) " +
            "FROM EndpointHit e " +
            "WHERE e.uri IN :uris AND e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAllByUriByDistinctIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
