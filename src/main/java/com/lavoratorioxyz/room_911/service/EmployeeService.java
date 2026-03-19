package com.lavoratorioxyz.room_911.service;

import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import com.lavoratorioxyz.room_911.entity.AccessResult;
import com.lavoratorioxyz.room_911.entity.Department;
import com.lavoratorioxyz.room_911.entity.Employee;
import com.lavoratorioxyz.room_911.repository.AccessAttemptRepository;
import com.lavoratorioxyz.room_911.repository.DepartmentRepository;
import com.lavoratorioxyz.room_911.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccessAttemptRepository accessAttemptRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           AccessAttemptRepository accessAttemptRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.accessAttemptRepository = accessAttemptRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public Optional<Employee> getByInternalId(String internalId) {
        return employeeRepository.findByInternalId(internalId);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setInternalId(employeeDetails.getInternalId());
        employee.setAccessEnabled(employeeDetails.getAccessEnabled());

        return employeeRepository.save(employee);
    }

    public Employee changeAccess(Long id, Boolean enabled) {
        Employee employee = getEmployeeById(id);

        employee.setAccessEnabled(enabled);

        return employeeRepository.save(employee);
    }

    public Employee deactivateEmployee(Long id) {
        Employee employee = getEmployeeById(id);

        employee.setActive(false);

        return employeeRepository.save(employee);
    }

    public Employee activateEmployee(Long id) {
        Employee employee = getEmployeeById(id);

        employee.setActive(true);

        return employeeRepository.save(employee);
    }

    public String validateAccess(String internalId) {

        Optional<Employee> employee = employeeRepository.findByInternalId(internalId);

        AccessAttempt attempt = new AccessAttempt();
        attempt.setEmployeeCode(internalId);

        if (employee.isEmpty()) {
            attempt.setResult(AccessResult.NOT_REGISTERED);
            accessAttemptRepository.save(attempt);
            return "NOT_REGISTERED";
        }

        if (!employee.get().getAccessEnabled()) {
            attempt.setResult(AccessResult.DENIED);
            accessAttemptRepository.save(attempt);
            return "DENIED";
        }

        attempt.setResult(AccessResult.AUTHORIZED);
        accessAttemptRepository.save(attempt);

        return "AUTHORIZED";
    }
    public Employee assignDepartment(Long employeeId, Long departmentId) {

        Employee employee = getEmployeeById(employeeId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }
}