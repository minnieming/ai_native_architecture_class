package com.sesac.aibackend.repository;

import com.sesac.aibackend.domain.Department;
import com.sesac.aibackend.domain.Team;
import org.apache.commons.lang3.ClassUtils;
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
