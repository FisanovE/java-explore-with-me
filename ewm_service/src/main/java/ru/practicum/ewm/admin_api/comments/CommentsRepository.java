package ru.practicum.ewm.admin_api.comments;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.admin_api.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.event.id = :id")
    List<Comment> findAllByEventId(Long id, PageRequest pageRequest);

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE (upper(c.text) like upper(concat('%', :text, '%')) OR :text IS null) " +
            "AND (c.user.id IN :users OR :users IS null) " +
            "AND (c.event.id IN :events OR :events IS null) " +
            "AND (c.created > :start OR CAST(:start AS LocalDateTime) IS null) " +
            "AND (c.created < :end OR CAST(:end AS LocalDateTime) IS null)")
    List<Comment> findAllByFilters(String text, List<Long> users, List<Long> events, LocalDateTime start,
                                   LocalDateTime end, PageRequest pageRequest);
}
