package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.repositories.ExamRepository;
import com.vesna1010.college.services.ExamService;

public class ExamServiceTest extends BaseServiceTest {

	@Autowired
	private ExamService service;
	@MockBean
	private ExamRepository repository;

	@Test
	public void findAllExamsByProfessorAndSubjectAndDateTest() {
		Professor professor = new Professor(1L, "Professor");
		Subject subject = new Subject(1L, "Subject");
		Student student = new Student(1L, "Student");
		Exam exam = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), student, subject, professor, 10);

		when(repository.findAllByProfessorAndSubjectAndDate(professor, subject, LocalDate.of(2019, Month.FEBRUARY, 10)))
				.thenReturn(Arrays.asList(exam));

		List<Exam> exams = service.findAllExamsByProfessorAndSubjectAndDate(professor, subject,
				LocalDate.of(2019, Month.FEBRUARY, 10));

		assertThat(exams, hasSize(1));
		assertTrue(exams.contains(exam));
		verify(repository, times(1)).findAllByProfessorAndSubjectAndDate(professor, subject,
				LocalDate.of(2019, Month.FEBRUARY, 10));
	}

	@Test
	public void findAllExamsByStudyProgramIdTest() {
		Sort sort = Sort.by(Order.asc("subject.id"), Order.asc("student.id"));
		Pageable pageable = PageRequest.of(0, 10, sort);
		Exam exam = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), new Student(1L, "Student"),
				new Subject(1L, "Subject"), new Professor(1L, "Professor"), 10);

		when(repository.findAllBySubjectStudyProgramId(1L, pageable))
				.thenReturn(new PageImpl<Exam>(Arrays.asList(exam)));

		Page<Exam> page = service.findAllExamsByStudyProgramId(1L, pageable);
		List<Exam> exams = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(exams, hasSize(1));
		assertThat(exams.get(0).getScore(), is(10));
		verify(repository, times(1)).findAllBySubjectStudyProgramId(1L, pageable);
	}

	@Test
	public void findExamByIdTest() {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);

		when(repository.findById(id)).thenReturn(Optional.of(new Exam(LocalDate.of(2019, Month.FEBRUARY, 10),
				new Student(1L, "Student"), new Subject(1L, "Subject"), new Professor(1L, "Professor"), 10)));

		Exam exam = service.findExamById(id);

		assertThat(exam.getScore(), is(10));
		verify(repository, times(1)).findById(id);
	}

	@Test(expected = RuntimeException.class)
	public void findExamByIdNotFoundTest() {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);

		when(repository.findById(id)).thenReturn(Optional.empty());

		service.findExamById(id);
	}

	@Test
	public void saveExamTest() {
		Exam exam = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), new Student(1L, "Student"),
				new Subject(1L, "Subject"), new Professor(1L, "Professor"), 10);

		when(repository.save(exam)).thenReturn(exam);

		Exam examSaved = service.saveExam(exam);

		assertThat(examSaved.getScore(), is(10));
		verify(repository, times(1)).save(exam);
	}

	@Test
	public void deleteExamById() {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);

		doNothing().when(repository).deleteById(id);

		service.deleteExamById(id);

		verify(repository, times(1)).deleteById(id);
	}

}
