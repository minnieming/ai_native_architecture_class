package com.sesac.aibackend.sp02_hw_company.service;

import com.sesac.aibackend.sp02_hw_company.domain.Employee;
import com.sesac.aibackend.sp02_hw_company.dto.EmployeeRequest;
import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import com.sesac.aibackend.sp02_hw_company.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Creat
    @Transactional
    public Employee save(EmployeeRequest req) {
        return employeeRepository.save(req.toEntity()); // repository까지 가지 않아도 된다.
    }

    // Read
    @Transactional (readOnly = true)
    public Optional<Employee> findById(Long id) { // controller 단에서 예외처리 하기 (서비스에서 하는게 아닌가..?)
        return employeeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findByUsername(String name) {
        return employeeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    // Update
    @Transactional
    public Optional<Employee> update(Long id, String name) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("employee ", id));

        employee.setName(name);
        return employeeRepository.findById(id);
    }

    // Delete
    @Transactional
    public void delete(Long id) {

        employeeRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("employee ", id));

        employeeRepository.deleteById(id);
    }
}
