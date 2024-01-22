package ru.practicum.ewm.admin_api.events;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.admin_api.events.model.Event;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface EventsRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id = :id")
    List<Event> findAllByInitiator(long id, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.initiator.id = :user AND e.id = :event")
    Optional<Event> findByIdAndInitiator(long user, long event);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.initiator.id IN :users OR :users IS null) " +
            "AND (e.state IN :states OR :states IS null) " +
            "AND (e.category.id IN :categories OR :categories IS null) " +
            "AND (e.eventDate > :start OR CAST(:start AS LocalDateTime) IS null) " +
            "AND (e.eventDate < :end OR CAST(:end AS LocalDateTime) IS null) ")
    List<Event> findAllByParam(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime start,
                               LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.state = 'PUBLISHED') " +
            "AND (upper(e.annotation) like upper(concat('%', :text, '%')) OR upper(e.description) like upper(concat('%', :text, '%')) " +
            "OR :text IS null) " +
            "AND (e.category.id IN :categories OR :categories IS null) " +
            "AND (e.paid = :paid OR :paid IS null) " +
            "AND (e.eventDate > :start OR CAST(:start AS LocalDateTime) IS null) " +
            "AND (e.eventDate < :end OR CAST(:end AS LocalDateTime) IS null) " +
            "AND (e.confirmedRequest <= e.participantLimit) ")
    List<Event> findAllByFilterIsAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                           LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE (e.state = 'PUBLISHED') " +
            "AND (upper(e.annotation) like upper(concat('%', :text, '%')) OR upper(e.description) like upper(concat('%', :text, '%')) " +
            "OR :text IS null) " +
            "AND (e.category.id IN :categories OR :categories IS null) " +
            "AND (e.paid = :paid OR :paid IS null) " +
            "AND (e.eventDate > :start OR CAST(:start AS LocalDateTime) IS null) " +
            "AND (e.eventDate < :end OR CAST(:end AS LocalDateTime) IS null) ")
    List<Event> findAllByFilterNotAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                            LocalDateTime end, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event e " +
            "WHERE e.id = :id " +
            "AND (e.state = 'PUBLISHED') ")
    Optional<Event> findByIdOnlyPublic(long id);
}
