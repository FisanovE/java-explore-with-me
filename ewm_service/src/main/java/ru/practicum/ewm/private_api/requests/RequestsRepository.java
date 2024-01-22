package ru.practicum.ewm.private_api.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.private_api.requests.model.Request;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r " +
            "FROM Request r " +
            "WHERE r.event.id = :id")
    List<Request> findAllByEventId(long id);

    @Query("SELECT r " +
            "FROM Request r " +
            "WHERE r.requester = :id")
    List<Request> findAllByUserId(long id);

    @Query("SELECT r " +
            "FROM Request r " +
            "WHERE r.event.initiator.id = :user AND r.event.id = :event")
    List<Request> findAllByEventIdAndInitiatorId(long user, long event);
}
