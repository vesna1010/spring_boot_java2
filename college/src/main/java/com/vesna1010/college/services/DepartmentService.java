package com.vesna1010.college.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.college.models.Department;

public interface DepartmentService {

	List<Department> findAllDepartments(Sort sort);

	Page<Department> findAllDepartments(Pageable pageable);

	Department findDepartmentById(Long id);

	Department saveDepartment(Department department);

	void deleteDepartmentById(Long id);

}
