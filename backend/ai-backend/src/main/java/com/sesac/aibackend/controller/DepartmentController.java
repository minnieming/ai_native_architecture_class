package com.sesac.aibackend.controller;

import com.sesac.aibackend.domain.Department;
import com.sesac.aibackend.dto.DepartmentRequest;
import com.sesac.aibackend.dto.DepartmentResponse;
import com.sesac.aibackend.error.NotFoundException;
import com.sesac.aibackend.repository.DepartmentRepository;
import com.sesac.aibackend.service.DepartmentService;
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
