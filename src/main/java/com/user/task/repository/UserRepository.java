package com.user.task.repository;

import com.user.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.birthDate > ?1 AND u.birthDate < ?2")
    List<User> findAllBetweenDates(LocalDate from, LocalDate to);
}
