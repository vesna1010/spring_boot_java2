package com.vesna1010.college.services.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.repositories.StudentRepository;
import com.vesna1010.college.services.StudentService;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository repository;

	@Override
	public List<Student> findAllStudentsByStudyProgramId(Long id, Sort sort) {
		return repository.findAllByStudyProgramId(id, sort);
	}

	@Override
	public Page<Student> findAllStudents(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Student> findAllStudentsByStudyProgramId(Long id, Pageable pageable) {
		return repository.findAllByStudyProgramId(id, pageable);
	}

	@Override
	public Student findStudentById(Long id) {
		Optional<Student> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No student found with id " + id));
	}

	@Override
	public Student saveStudent(Student student) {
		return repository.save(student);
	}

	@Override
	public void deleteStudentById(Long id) {
		repository.deleteById(id);
	}

}
