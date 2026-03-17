package com.lavoratorioxyz.room_911.repository;

import com.lavoratorioxyz.room_911.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    Optional<Employee> findByInternalId(String internalId);

    List<Employee> findByActiveTrue();

    List<Employee> findByActiveFalse();

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByLastNameContainingIgnoreCase(String lastName);
}