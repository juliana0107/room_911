package com.lavoratorioxyz.room_911.repository;

import com.lavoratorioxyz.room_911.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);
}