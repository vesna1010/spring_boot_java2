package com.vesna1010.college.services;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;

public interface ExamService {

	List<Exam> findAllExamsByProfessorAndSubjectAndDate(Professor professor, Subject subject, LocalDate date);

	Page<Exam> findAllExamsByStudyProgramId(Long id, Pageable pageable);

	Exam findExamById(StudentSubjectId id);

	Exam saveExam(Exam exam);

	void deleteExamById(StudentSubjectId id);

}
