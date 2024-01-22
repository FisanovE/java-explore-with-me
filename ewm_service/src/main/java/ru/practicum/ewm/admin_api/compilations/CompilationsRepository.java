package ru.practicum.ewm.admin_api.compilations;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.admin_api.compilations.model.Compilation;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c " +
            "FROM Compilation c " +
            "WHERE c.pinned = :pinned")
    List<Compilation> findAllByPinned(boolean pinned, PageRequest pageRequest);
}