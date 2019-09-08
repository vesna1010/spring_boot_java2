package com.vesna1010.college.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.models.Exam;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.Student;
import com.vesna1010.college.models.StudentSubjectId;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.services.ExamService;
import com.vesna1010.college.services.ProfessorService;
import com.vesna1010.college.services.StudentService;
import com.vesna1010.college.services.SubjectService;

public class ExamsControllerTest extends BaseControllerTest {

	@MockBean
	private ExamService examService;
	@MockBean
	private ProfessorService professorService;
	@MockBean
	private SubjectService subjectService;
	@MockBean
	private StudentService studentService;
	
	@Test
	@WithAnonymousUser
	public void renderPageWithExamsByStudyProgramWithAnonymousUserTest() throws Exception {
		renderPageWithExamsByStudyProgramIdNotLoggedIn();
	}
	
	private void renderPageWithExamsByStudyProgramIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/exams")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithExamsByStudyProgramWithAuthorityProfessorTest() throws Exception {
		renderPageWithExamsByStudyProgramId();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithExamsByStudyProgramWithAuthorityUserTest() throws Exception {
		renderPageWithExamsByStudyProgramId();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithExamsByStudyProgramWithAuthorityAdminTest() throws Exception {
		renderPageWithExamsByStudyProgramId();
	}
	
	private void renderPageWithExamsByStudyProgramId() throws Exception {
		Sort sort = Sort.by(Order.asc("subject.id"), Order.asc("student.id"));
		Pageable pageable = PageRequest.of(0, 10, sort);
		List<Exam> exams = Arrays.asList(new Exam(LocalDate.now(), new Student(1L, "Student"),
				new Subject(1L, "Subject"), new Professor(1L, "Professor"), 10));

		when(examService.findAllExamsByStudyProgramId(1L, pageable)).thenReturn(new PageImpl<Exam>(exams));

		mockMvc.perform(
				get("/exams")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(1))))
		       .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
		       .andExpect(view().name("exams/page"));
		
		verify(examService, times(1)).findAllExamsByStudyProgramId(1L, pageable);
	}
	
	@Test
	@WithAnonymousUser
	public void deleteExamByIdWithAnonymousUserTest() throws Exception {
		deleteExamByIdNotLoggedIn();
	}
	
	private void deleteExamByIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/exams/delete")
				.param("id", "1")
				.param("subjectId", "1")
				.param("studentId", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteExamByIdWithAuthorityProfessorTest() throws Exception {
		deleteExamByIdNotAuthorized();
	}
	
	private void deleteExamByIdNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/exams/delete")
				.param("id", "1")
				.param("subjectId", "1")
				.param("studentId", "1")
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteExamByIdWithAuthorityUserTest() throws Exception {
		deleteExamById();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteExamByIdWithAuthorityAdminTest() throws Exception {
		deleteExamById();
	}
	
	private void deleteExamById() throws Exception {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		
		doNothing().when(examService).deleteExamById(id);
		
		mockMvc.perform(
				get("/exams/delete")
				.param("id", "1")
				.param("subjectId", "1")
				.param("studentId", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/exams?id=1"));
	
		verify(examService, times(1)).deleteExamById(id);
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}

	private void renderEmptyFormNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/exams/form")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/exams/form")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderEmptyFormWithAuthorityUserTest() throws Exception {
		renderEmptyForm();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderEmptyFormWithAuthorityAdminTest() throws Exception {
		renderEmptyForm();
	}
	
	private void renderEmptyForm() throws Exception {
		List<Subject> subjects = Arrays.asList(new Subject(1L, "Subject A"), new Subject(2L, "Subject B"));
		List<Professor> professors = Arrays.asList(new Professor(1L, "Professor A"), new Professor(2L, "Professor B"));
		List<Student> students = Arrays.asList(new Student(1L, "Student A"), new Student(2L, "Student B"));

		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(subjects);
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(students);
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT)).thenReturn(professors);

		mockMvc.perform(
				get("/exams/form")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("exam", is(new Exam())))
		       .andExpect(model().attribute("professors", hasSize(2)))
		       .andExpect(model().attribute("subjects", hasSize(2)))
		       .andExpect(model().attribute("students", hasSize(2)))
		       .andExpect(view().name("exams/form"));
		
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}
	
	@Test
	@WithAnonymousUser
	public void saveExamWithAnonymousUserTest() throws Exception {
		saveExamNotLoggedIn();
	}
	
	private void saveExamNotLoggedIn() throws Exception {
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "1")
				.param("subject", "1")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveExamWithAuthorityProfessorTest() throws Exception {
		saveExamNotAuthorized();
	}
	
	private void saveExamNotAuthorized() throws Exception {
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "1")
				.param("subject", "1")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveExamWithAuthorityUserTest() throws Exception {
		saveExam();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveExamWithAuthorityAdminTest() throws Exception {
		saveExam();
	}
	
	private void saveExam() throws Exception {
		Student student = new Student(1L, "Student");
		Subject subject = new Subject(1L, "Subject");
		Professor professor = new Professor(1L, "Professor");
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student, subject, professor, 10);
		
		when(studentService.findStudentById(1L)).thenReturn(student);
		when(subjectService.findSubjectById(1L)).thenReturn(subject);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(examService.saveExam(exam)).thenReturn(exam);

		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "1")
				.param("subject", "1")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/exams/form?id=1"));
		
		verify(studentService, times(1)).findStudentById(1L);
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(examService, times(1)).saveExam(exam);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveExamInvalidFormTest() throws Exception {
		Student student = new Student(1L, "Student");
		Subject subject = new Subject(1L, "Subject");
		Professor professor = new Professor(1L, "Professor");
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student, subject, professor, 5);
		
		when(studentService.findStudentById(1L)).thenReturn(student);
		when(subjectService.findSubjectById(1L)).thenReturn(subject);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(examService.saveExam(exam)).thenReturn(exam);
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(professor));
		
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "1")
				.param("subject", "1")
				.param("professor", "1")
				.param("score", "5")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("exam", "score"))
		       .andExpect(model().attribute("exam", hasProperty("student", is(student))))
		       .andExpect(model().attribute("exam", hasProperty("subject", is(subject))))
		       .andExpect(model().attribute("professors", hasSize(1)))
		       .andExpect(model().attribute("subjects", hasSize(1)))
		       .andExpect(model().attribute("students", hasSize(1)))
		       .andExpect(view().name("exams/form"));
		
		verify(studentService, times(2)).findStudentById(1L);
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(2)).findProfessorById(1L);
		verify(examService, times(0)).saveExam(exam);
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}

	@Test
	@WithAnonymousUser
	public void renderFormWithExamWithAnonymousUserTest() throws Exception {
		renderFormWithExamNotLoggedIn();
	}
	
	private void renderFormWithExamNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderFormWithExamWithAuthorityProfessorTest() throws Exception {
		renderFormWithExamNotAuthorized();
	}
	
	private void renderFormWithExamNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithExamWithAuthorityUserTest() throws Exception {
		renderFormWithExam();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderFormWithExamWithAuthorityAdminTest() throws Exception {
		renderFormWithExam();
	}
	
	private void renderFormWithExam() throws Exception {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		Student student = new Student(1L, "Student");
		Subject subject = new Subject(1L, "Subject");
		Professor professor = new Professor(1L, "Professor");
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student, subject, professor, 10);
		
		when(examService.findExamById(id)).thenReturn(exam);
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(professor));
		
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("exam", hasProperty("score", is(10))))
		       .andExpect(model().attribute("professors", hasSize(1)))
		       .andExpect(model().attribute("subjects", hasSize(1)))
		       .andExpect(model().attribute("students", hasSize(1)))
		       .andExpect(view().name("exams/form"));
		
		verify(examService, times(1)).findExamById(id);
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}

	@Test
	@WithAnonymousUser
	public void renderSearchExamFormWithAnonymousUserTest() throws Exception {
		renderSearchExamFormNotLoggedIn();
	}
	
	private void renderSearchExamFormNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/exams/search")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderSearchExamFormWithAuthorityProfessorTest() throws Exception {
		renderSearchExamForm();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderSearchExamFormWithAuthorityUserTest() throws Exception {
		renderSearchExamForm();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderSearchExamFormWithAuthorityAdminTest() throws Exception {
		renderSearchExamForm();
	}
	
	private void renderSearchExamForm() throws Exception {
		List<Subject> subjects = Arrays.asList(new Subject(1L, "Subject A"), new Subject(2L, "Subject B"));
		List<Professor> professors = Arrays.asList(new Professor(1L, "Professor A"), new Professor(2L, "Professor B"));

		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(subjects);
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT)).thenReturn(professors);

		mockMvc.perform(
				get("/exams/search")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("exam", is(new Exam())))
		       .andExpect(model().attribute("professors", hasSize(2)))
		       .andExpect(model().attribute("subjects", hasSize(2)))
		       .andExpect(view().name("exams/search"));
		
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}
	
	@WithAnonymousUser
	public void renderSearchedExamsWithAnonymousUserTest() throws Exception {
		renderSearchedExamsNotLoggedIn();
	}
	
	private void renderSearchedExamsNotLoggedIn() throws Exception {
		mockMvc.perform(
				post("/exams/search")
				.param("date", "10.02.2019")
				.param("subject", "1")
				.param("professor", "1")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@WithMockUser(authorities = "PROFESSOR")
	public void renderSearchedExamsWithAuthorityProfessorTest() throws Exception {
		renderSearchedExams();
	}
	
	@WithMockUser(authorities = "USER")
	public void renderSearchedExamsWithAuthorityUserTest() throws Exception {
		renderSearchedExams();
	}
	
	@WithMockUser(authorities = "ADMIN")
	public void renderSearchedExamsWithAuthorityAdminTest() throws Exception {
		renderSearchedExams();
	}
	
	private void renderSearchedExams() throws Exception {
		Subject subject = new Subject(1L, "Subject");
		Professor professor = new Professor(1L, "Professor");
		Student student = new Student(1L, "Student");
		Exam exam = new Exam(LocalDate.of(2019, Month.FEBRUARY, 10), student, subject, professor, 10);
	
		when(subjectService.findSubjectById(1L)).thenReturn(subject);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(examService.findAllExamsByProfessorAndSubjectAndDate(professor, subject,
				LocalDate.of(2019, Month.FEBRUARY, 10))).thenReturn(Arrays.asList(exam));
	
		mockMvc.perform(
				post("/exams/search")
				.param("date", "10.02.2019")
				.param("subject", "1")
				.param("professor", "1")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("subject", is(subject)))
		       .andExpect(model().attribute("professor", is(professor)))
		       .andExpect(model().attribute("date", is(LocalDate.of(2019, Month.FEBRUARY, 10))))
		       .andExpect(model().attribute("exams", hasSize(1)))
		       .andExpect(view().name("exams/searched"));
		
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(examService, times(1)).findAllExamsByProfessorAndSubjectAndDate(professor, subject,
				LocalDate.of(2019, Month.FEBRUARY, 10));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderSearchedExamsInvalidFormTest() throws Exception {
		Subject subject = new Subject(1L, "Subject");
		Professor professor = new Professor(1L, "Professor");
	
		when(subjectService.findSubjectById(1L)).thenReturn(subject);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(professor));

		mockMvc.perform(
				post("/exams/search")
				.param("date", "")
				.param("subject", "1")
				.param("professor", "1")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("exam", "date"))
		       .andExpect(model().attribute("professors", hasSize(1)))
		       .andExpect(model().attribute("subjects", hasSize(1)))
		       .andExpect(model().attribute("exam", hasProperty("subject", is(subject))))
		       .andExpect(model().attribute("exam", hasProperty("professor", is(professor))))
		       .andExpect(view().name("exams/search"));
		
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(2)).findProfessorById(1L);
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}
	
}
