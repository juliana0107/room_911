package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.entity.Employee;
import com.lavoratorioxyz.room_911.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // Listar empleados
    @GetMapping
    public List<Employee> getAll() {
        return service.getEmployees();
    }

    // Crear empleado
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.saveEmployee(employee);
    }

    // Actualizar
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee employee) {
        return service.updateEmployee(id, employee);
    }

    // Inactivar
    @PutMapping("/{id}/deactivate")
    public Employee deactivate(@PathVariable Long id) {
        return service.deactivateEmployee(id);
    }

    // Cambiar acceso
    @PutMapping("/{id}/access")
    public Employee changeAccess(@PathVariable Long id, @RequestParam Boolean enabled) {
        return service.changeAccess(id, enabled);
    }

    @GetMapping("/validate")
    public Map<String, String> validate(@RequestParam String internalId) {
        String result = service.validateAccess(internalId);
        return Map.of("result", result);
    }

    @PutMapping("/{id}/department/{departmentId}")
    public Employee assignDepartment(
            @PathVariable Long id,
            @PathVariable Long departmentId) {

        return service.assignDepartment(id, departmentId);
    }

    @GetMapping("/department/{departmentId}")
    public List<Employee> getByDepartment(@PathVariable Long departmentId) {
        return service.getByDepartment(departmentId);
    }
}