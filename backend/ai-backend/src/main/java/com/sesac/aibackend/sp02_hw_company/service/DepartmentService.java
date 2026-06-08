package com.sesac.aibackend.sp02_hw_company.service;

import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import com.sesac.aibackend.sp02_hw_company.domain.Department;
import com.sesac.aibackend.sp02_hw_company.domain.Employee;
import com.sesac.aibackend.sp02_hw_company.domain.Team;
import com.sesac.aibackend.sp02_hw_company.dto.DepartmentRequest;
import com.sesac.aibackend.sp02_hw_company.repository.DepartmentRepository;
import com.sesac.aibackend.sp02_hw_company.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    // Creat
    @Transactional
    public Department save(DepartmentRequest req) {

        Employee employee = employeeRepository.findById(req.employeeId())
                .orElseThrow(() -> NotFoundException.of("employee ", req.employeeId()));

        Team teams;
        try {
            teams = Team.valueOf(req.team().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("department " + req.team());
        }

        Department department = Department.builder()
                .employee(employee)
                .team(teams)
                .build();

        return departmentRepository.save(department);
    }

    // Read
    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {

        return departmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Department> findByTeam(String team) {

        Team teams;
        try {
            teams = Team.valueOf(team.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("department " + team);
        }

        return departmentRepository.findByTeam(teams);
    }

    @Transactional(readOnly = true)
    public List<Department> findByEmployee(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        return departmentRepository.findByEmployeeId(id);
    }

    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    // Update
    @Transactional
    public Optional<Department> update(Long id, String team) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        Team teams;
        try {
            teams = Team.valueOf(team.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("department " + team);
        }

        department.setTeam(teams);
        return departmentRepository.findById(id);
    }

    // Delete
    @Transactional
    public void delete(Long id) {

        departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        departmentRepository.deleteById(id);
    }
}
