package com.vesna1010.college.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.repositories.ExamRepository;
import com.vesna1010.college.services.ExamService;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

	@Autowired
	private ExamRepository repository;

	@Override
	public List<Exam> findAllExamsByProfessorAndSubjectAndDate(Professor professor, Subject subject, LocalDate date) {
		return repository.findAllByProfessorAndSubjectAndDate(professor, subject, date);
	}

	@Override
	public Page<Exam> findAllExamsByStudyProgramId(Long id, Pageable pageable) {
		return repository.findAllBySubjectStudyProgramId(id, pageable);
	}

	@Override
	public Exam findExamById(StudentSubjectId id) {
		Optional<Exam> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No exam found with id " + id));
	}

	@Override
	public Exam saveExam(Exam exam) {
		return repository.save(exam);
	}

	@Override
	public void deleteExamById(StudentSubjectId id) {
		repository.deleteById(id);
	}

}
