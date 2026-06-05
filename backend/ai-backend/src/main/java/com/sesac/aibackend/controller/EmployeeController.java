package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Employee;
import com.sesac.aibackend.dto.EmployeeRequest;
import com.sesac.aibackend.dto.EmployeeResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.EmployeeRepository;
import com.sesac.aibackend.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<EmployeeResponse> save(@Valid @RequestBody EmployeeRequest req) {

        Employee saved = employeeService.save(req);

        URI location = URI.create("/employees/ " + saved.getId());

        return ResponseEntity.created(location).body(EmployeeResponse.from(saved));
    }

    @GetMapping("/{id}")
    public EmployeeResponse findById(@PathVariable Long id) {

        employeeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("employee ", id));

        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> NotFoundException.of("employee ", id));

        return EmployeeResponse.from(employee);
    }

    @GetMapping("/name/{name}")
    public EmployeeResponse findByUsername(@PathVariable String name) {

        Employee employee = employeeService.findByUsername(name)
                .orElseThrow(() -> NotFoundException.of("employee ", name));

        return EmployeeResponse.from(employee);
    }

    @GetMapping
    public List<EmployeeResponse> list() {
        return employeeService.findAll().stream().map(EmployeeResponse::from).toList();
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest req) {

        employeeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("employee ", id));

        Employee employee = employeeService.update(id, req.name())
                .orElseThrow(() -> NotFoundException.of("employee ", id));

        return EmployeeResponse.from(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        employeeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("employee ", id));

        employeeService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
