package com.vesna1010.college.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.college.models.Student;

public interface StudentService {

	List<Student> findAllStudentsByStudyProgramId(Long id, Sort sort);

	Page<Student> findAllStudents(Pageable pageable);

	Page<Student> findAllStudentsByStudyProgramId(Long id, Pageable pageable);

	Student findStudentById(Long id);

	Student saveStudent(Student student);

	void deleteStudentById(Long id);

}
