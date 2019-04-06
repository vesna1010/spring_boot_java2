package com.vesna1010.college.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.college.models.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
