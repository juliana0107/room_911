package com.lavoratorioxyz.room_911.repository;

import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessAttemptRepository extends JpaRepository<AccessAttempt, Long> {

    List<AccessAttempt> findByAttemptTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a.result, COUNT(a) FROM AccessAttempt a GROUP BY a.result")
    List<Object[]> countByResult();

    @Query("SELECT a.result, COUNT(a) FROM AccessAttempt a WHERE a.attemptTime BETWEEN :start AND :end GROUP BY a.result")
    List<Object[]> countByResultBetween(LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT a FROM AccessAttempt a
    JOIN Employee e ON a.employeeCode = e.internalId
    WHERE e.department.id = :departmentId
""")
    List<AccessAttempt> findByDepartmentId(Long departmentId);

}


