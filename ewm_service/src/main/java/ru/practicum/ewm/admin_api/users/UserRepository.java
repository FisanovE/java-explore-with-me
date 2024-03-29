package ru.practicum.ewm.admin_api.users;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.admin_api.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u " +
            "FROM User u ")
    List<User> findAllUsers(Pageable pageable);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id IN :ids ")
    List<User> findAllById(List<Long> ids, Pageable pageable);
}
