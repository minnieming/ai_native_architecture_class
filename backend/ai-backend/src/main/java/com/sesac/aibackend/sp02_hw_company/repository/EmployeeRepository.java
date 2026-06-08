package com.sesac.aibackend.sp02_hw_company.repository;

import com.sesac.aibackend.sp02_hw_company.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByName(String name);
}
