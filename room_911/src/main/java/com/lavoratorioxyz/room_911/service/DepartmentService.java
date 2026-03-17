package com.lavoratorioxyz.room_911.service;

import com.lavoratorioxyz.room_911.entity.Department;
import com.lavoratorioxyz.room_911.repository.DepartmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<Department> getAll() {
        return repository.findAll();
    }

    public Department getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public Department create(Department department) {

        if (department.getName() == null || department.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
        department.setName(department.getName().trim());
        if (repository.findByName(department.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El departamento ya existe");
        }
        return repository.save(department);
    }

    public Department update(Long id, Department data) {
        Department dept = getById(id);

        dept.setName(data.getName());
        dept.setDescription(data.getDescription());

        return repository.save(dept);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}