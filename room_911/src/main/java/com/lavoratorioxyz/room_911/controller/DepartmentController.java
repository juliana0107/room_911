package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.entity.Department;
import com.lavoratorioxyz.room_911.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    // Listar
    @GetMapping
    public List<Department> getAll() {
        return service.getAll();
    }

    // Crear
    @PostMapping
    public Department create(@RequestBody Department department) {
        return service.create(department);
    }

    // Actualizar
    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        return service.update(id, department);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}