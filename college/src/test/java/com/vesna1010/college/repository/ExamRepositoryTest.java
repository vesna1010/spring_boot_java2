package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.repositories.ExamRepository;

public class ExamRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private ExamRepository repository;

	@Test
	public void findAllExamsByStudyProgramIdTest() {
		Sort sort = Sort.by(Order.asc("subject.id"), Order.asc("student.id"));
		Page<Exam> page = repository.findAllBySubjectStudyProgramId(1L, PageRequest.of(0, 10, sort));
		List<Exam> exams = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(exams, hasSize(3));
		assertThat(exams.get(0).getScore(), is(8));
		assertThat(exams.get(1).getScore(), is(7));
		assertThat(exams.get(2).getScore(), is(9));
	}

	@Test
	public void findAllByProfessorAndSubjectAndDateTest() {
		List<Exam> exams = repository.findAllByProfessorAndSubjectAndDate(professor1, subject1,
				LocalDate.of(2019, Month.FEBRUARY, 10));

		assertThat(exams, hasSize(1));
		assertTrue(exams.contains(exam1));
	}

	@Test
	public void findExamByIdTest() {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		Optional<Exam> optional = repository.findById(id);
		Exam exam = optional.get();

		assertThat(exam.getProfessor(), is(professor1));
		assertThat(exam.getScore(), is(8));
	}

	@Test
	public void findExamByIdNotFoundTest() {
		StudentSubjectId id = new StudentSubjectId(1L, 2L);
		Optional<Exam> optional = repository.findById(id);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveExamTest() {
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10);

		exam = repository.save(exam);

		StudentSubjectId id = new StudentSubjectId(2L, 3L);

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(5L));
	}

	@Test
	public void updateExamTest() {
		exam1.setScore(10);
		exam1 = repository.save(exam1);

		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		Optional<Exam> optional = repository.findById(id);
		Exam exam = optional.get();

		assertThat(exam.getScore(), is(10));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void deleteSubjectByIdTest() {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		repository.deleteById(id);

		assertFalse(repository.existsById(id));
		assertThat(repository.count(), is(3L));
	}

}
