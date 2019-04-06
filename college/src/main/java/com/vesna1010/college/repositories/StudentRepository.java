package com.vesna1010.college.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.college.models.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	List<Student> findAllByStudyProgramId(Long id, Sort sort);

	Page<Student> findAllByStudyProgramId(Long id, Pageable pageable);

}
