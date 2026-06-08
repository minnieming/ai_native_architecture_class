package com.sesac.aibackend.sp02_hw_company.repository;

import com.sesac.aibackend.sp02_hw_company.domain.Department;
import com.sesac.aibackend.sp02_hw_company.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByTeam(Team team);

    @Query("""
        select d from Department d
        join fetch d.employee
        where d.employee.id = :employeeId
            """)
    List<Department> findByEmployeeId(@Param("employeeId") Long employeeId);
}
