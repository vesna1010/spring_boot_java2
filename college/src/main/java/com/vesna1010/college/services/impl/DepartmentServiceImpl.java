package com.vesna1010.college.services.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.Department;
import com.vesna1010.college.repositories.DepartmentRepository;
import com.vesna1010.college.services.DepartmentService;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository repository;

	@Override
	public List<Department> findAllDepartments(Sort sort) {
		return repository.findAll(sort);
	}

	@Override
	public Page<Department> findAllDepartments(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Department findDepartmentById(Long id) {
		Optional<Department> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No department found with id " + id));
	}

	@Override
	public Department saveDepartment(Department department) {
		return repository.save(department);
	}

	@Override
	public void deleteDepartmentById(Long id) {
		repository.deleteById(id);
	}

}
