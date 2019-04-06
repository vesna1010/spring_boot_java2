package com.vesna1010.college.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;

public interface ExamRepository extends JpaRepository<Exam, StudentSubjectId> {

	List<Exam> findAllByProfessorAndSubjectAndDate(Professor professor, Subject subject, LocalDate date);

	Page<Exam> findAllBySubjectStudyProgramId(Long id, Pageable pageable);
	
}
