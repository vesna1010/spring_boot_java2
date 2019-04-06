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
import com.vesna1010.college.models.StudentSubjectId;
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
		Sort sort = Sort.by(Order.asc("subject.id"), Order.asc("student.id"));
		Pageable pageable = PageRequest.of(0, 10, sort);

		when(examService.findAllExamsByStudyProgramId(1L, pageable))
				.thenReturn(new PageImpl<Exam>(Arrays.asList(exam1, exam3, exam2)));	
		
		mockMvc.perform(
				get("/exams")
				.param("id", "1")
				)
		      .andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrlPattern("**/login"));
		
		verify(examService, times(0)).findAllExamsByStudyProgramId(1L, pageable);
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

		when(examService.findAllExamsByStudyProgramId(1L, pageable))
				.thenReturn(new PageImpl<Exam>(Arrays.asList(exam1, exam3, exam2)));	
		
		mockMvc.perform(
				get("/exams")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("page", hasProperty("content", hasSize(3))))
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
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		
		doNothing().when(examService).deleteExamById(id);
		
		mockMvc.perform(
				get("/exams/delete")
				.param("id", "1")
				.param("subjectId", "1")
				.param("studentId", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(examService, times(0)).deleteExamById(id);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteExamByIdWithAuthorityProfessorTest() throws Exception {
		deleteExamByIdNotAuthorized();
	}
	
	private void deleteExamByIdNotAuthorized() throws Exception {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		
		doNothing().when(examService).deleteExamById(id);
		
		mockMvc.perform(
				get("/exams/delete")
				.param("id", "1")
				.param("subjectId", "1")
				.param("studentId", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(examService, times(0)).deleteExamById(id);
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
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		
		mockMvc.perform(
				get("/exams/form")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(0)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(0)).findAllProfessorsByStudyProgramId(1L, SORT);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		
		mockMvc.perform(
				get("/exams/form")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(0)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(0)).findAllProfessorsByStudyProgramId(1L, SORT);
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
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		
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
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10);
		
		when(studentService.findStudentById(2L)).thenReturn(student2);
		when(subjectService.findSubjectById(3L)).thenReturn(subject3);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.saveExam(exam))
				.thenReturn(new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10));
	
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "2")
				.param("subject", "3")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studentService, times(0)).findStudentById(2L);
		verify(subjectService, times(0)).findSubjectById(3L);
		verify(professorService, times(0)).findProfessorById(1L);
		verify(examService, times(0)).saveExam(exam);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveExamWithAuthorityProfessorTest() throws Exception {
		saveExamNotAuthorized();
	}
	
	private void saveExamNotAuthorized() throws Exception {
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10);
		
		when(studentService.findStudentById(2L)).thenReturn(student2);
		when(subjectService.findSubjectById(3L)).thenReturn(subject3);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.saveExam(exam))
				.thenReturn(new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10));
	
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "2")
				.param("subject", "3")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
		
		verify(studentService, times(0)).findStudentById(2L);
		verify(subjectService, times(0)).findSubjectById(3L);
		verify(professorService, times(0)).findProfessorById(1L);
		verify(examService, times(0)).saveExam(exam);
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
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10);
		
		when(studentService.findStudentById(2L)).thenReturn(student2);
		when(subjectService.findSubjectById(3L)).thenReturn(subject3);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.saveExam(exam))
				.thenReturn(new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10));
		
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "2")
				.param("subject", "3")
				.param("professor", "1")
				.param("score", "10")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/exams/form?id=1"));
		
		verify(studentService, times(1)).findStudentById(2L);
		verify(subjectService, times(1)).findSubjectById(3L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(examService, times(1)).saveExam(exam);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveExamInvalidFormTest() throws Exception {
		Exam exam = new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 5);
		
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		when(studentService.findStudentById(2L)).thenReturn(student2);
		when(subjectService.findSubjectById(3L)).thenReturn(subject3);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.saveExam(exam))
				.thenReturn(new Exam(LocalDate.of(2019, 05, 10), student2, subject3, professor1, 10));
		
		mockMvc.perform(
				post("/exams/save")
				.param("date", "10.05.2019")
				.param("student", "2")
				.param("subject", "3")
				.param("professor", "1")
				.param("score", "5")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("exam", "score"))
		       .andExpect(model().attribute("exam", hasProperty("student", is(student2))))
		       .andExpect(model().attribute("exam", hasProperty("subject", is(subject3))))
		       .andExpect(model().attribute("professors", hasSize(2)))
               .andExpect(model().attribute("subjects", hasSize(2)))
               .andExpect(model().attribute("students", hasSize(2)))
		       .andExpect(view().name("exams/form"));
		       
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
		verify(studentService, times(1)).findStudentById(2L);
		verify(subjectService, times(1)).findSubjectById(3L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(examService, times(0)).saveExam(exam);
	}

	@Test
	@WithAnonymousUser
	public void renderFormWithExamWithAnonymousUserTest() throws Exception {
		renderFormWithExamNotLoggedIn();
	}
	
	private void renderFormWithExamNotLoggedIn() throws Exception {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		when(examService.findExamById(id)).thenReturn(exam1);
		
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(0)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(0)).findAllProfessorsByStudyProgramId(1L, SORT);
		verify(examService, times(0)).findExamById(id);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderFormWithExamWithAuthorityProfessorTest() throws Exception {
		renderFormWithExamNotAuthorized();
	}
	
	private void renderFormWithExamNotAuthorized() throws Exception {
		StudentSubjectId id = new StudentSubjectId(1L, 1L);
		
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		when(examService.findExamById(id)).thenReturn(exam1);
		
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().isForbidden());
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(0)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(0)).findAllProfessorsByStudyProgramId(1L, SORT);
		verify(examService, times(0)).findExamById(id);
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
		
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(studentService.findAllStudentsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(student2, student1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		when(examService.findExamById(id)).thenReturn(exam1);
		
		mockMvc.perform(
				get("/exams/edit")
				.param("id", "1")
				.param("studentId", "1")
				.param("subjectId", "1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("exam", hasProperty("score", is(8))))
               .andExpect(model().attribute("professors", hasSize(2)))
               .andExpect(model().attribute("subjects", hasSize(2)))
               .andExpect(model().attribute("students", hasSize(2)))
               .andExpect(view().name("exams/form"));
		
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(studentService, times(1)).findAllStudentsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
		verify(examService, times(1)).findExamById(id);
	}

	@Test
	@WithAnonymousUser
	public void renderSearchExamFormWithAnonymousUserTest() throws Exception {
		renderSearchExamFormNotLoggedIn();
	}
	
	private void renderSearchExamFormNotLoggedIn() throws Exception {
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		
		mockMvc.perform(
				get("/exams/search")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(0)).findAllProfessorsByStudyProgramId(1L, SORT);
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
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
		
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
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.findAllExamsByProfessorAndSubjectAndDate(professor1, subject1,
				LocalDate.of(2019, Month.FEBRUARY, 10))).thenReturn(Arrays.asList(exam1));
	
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
		
		verify(subjectService, times(0)).findSubjectById(1L);
		verify(professorService, times(0)).findProfessorById(1L);
		verify(examService, times(0)).findAllExamsByProfessorAndSubjectAndDate(professor1, subject1,
				LocalDate.of(2019, Month.FEBRUARY, 10));
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
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(examService.findAllExamsByProfessorAndSubjectAndDate(professor1, subject1,
				LocalDate.of(2019, Month.FEBRUARY, 10))).thenReturn(Arrays.asList(exam1));
	
		mockMvc.perform(
				post("/exams/search")
				.param("date", "10.02.2019")
				.param("subject", "1")
				.param("professor", "1")
				.param("id", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("subject", is(subject1)))
		       .andExpect(model().attribute("professor", is(professor1)))
		       .andExpect(model().attribute("date", is(LocalDate.of(2019, Month.FEBRUARY, 10))))
		       .andExpect(model().attribute("exams", hasSize(1)))
		       .andExpect(view().name("exams/searched"));
		
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(examService, times(1)).findAllExamsByProfessorAndSubjectAndDate(professor1, subject1,
				LocalDate.of(2019, Month.FEBRUARY, 10));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderSearchedExamsInvalidFormTest() throws Exception {
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(subjectService.findAllSubjectsByStudyProgramId(1L, SORT)).thenReturn(Arrays.asList(subject3, subject1));
		when(professorService.findAllProfessorsByStudyProgramId(1L, SORT))
				.thenReturn(Arrays.asList(professor1, professor2));
	
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
		       .andExpect(model().attribute("professors", hasSize(2)))
               .andExpect(model().attribute("subjects", hasSize(2)))
               .andExpect(model().attribute("exam", hasProperty("subject", is(subject1))))
               .andExpect(model().attribute("exam", hasProperty("professor", is(professor1))))
		       .andExpect(view().name("exams/search"));
		
		verify(subjectService, times(1)).findSubjectById(1L);
		verify(professorService, times(1)).findProfessorById(1L);
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, SORT);
		verify(professorService, times(1)).findAllProfessorsByStudyProgramId(1L, SORT);
	}
	
}
