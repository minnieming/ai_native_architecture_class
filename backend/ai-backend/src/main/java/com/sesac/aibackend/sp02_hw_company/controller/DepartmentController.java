package com.sesac.aibackend.sp02_hw_company.controller;

import com.sesac.aibackend.sp01_restapi.error.NotFoundException;
import com.sesac.aibackend.sp02_hw_company.domain.Department;
import com.sesac.aibackend.sp02_hw_company.dto.DepartmentRequest;
import com.sesac.aibackend.sp02_hw_company.dto.DepartmentResponse;
import com.sesac.aibackend.sp02_hw_company.repository.DepartmentRepository;
import com.sesac.aibackend.sp02_hw_company.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    @PostMapping
    public ResponseEntity<DepartmentResponse> save(@Valid @RequestBody DepartmentRequest req) {

        Department saved = departmentService.save(req);

        URI location = URI.create("/departments " + saved.getId());

        return ResponseEntity.created(location).body(DepartmentResponse.from(saved));
    }

    @GetMapping("/{id}")
    public DepartmentResponse findById(@PathVariable Long id) {

        departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        Department department = departmentService.findById(id)
                .orElseThrow(() -> NotFoundException.of("department ", id));

        return DepartmentResponse.from(department);
    }

    @GetMapping("/team/{team}")
    public DepartmentResponse findByTeam(@PathVariable String team) {

        Department department = departmentService.findByTeam(team)
                .orElseThrow(() -> NotFoundException.of("department ", team));

        return DepartmentResponse.from(department);
    }

    @GetMapping
    public List<DepartmentResponse> list() {
        return departmentService.findAll().stream().map(DepartmentResponse::from).toList();
    }

    @PutMapping("/{id}")
    public DepartmentResponse updatd(@PathVariable Long id, @Valid @RequestBody DepartmentRequest req) {

        departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        Department department = departmentService.update(id, req.team())
                .orElseThrow(() -> NotFoundException.of("department ", req.team()));

        return DepartmentResponse.from(department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        departmentRepository.findById(id)
                .orElseThrow(()-> NotFoundException.of("department ", id));

        departmentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
