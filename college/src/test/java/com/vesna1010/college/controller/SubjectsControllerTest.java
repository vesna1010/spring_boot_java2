package com.vesna1010.college.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.models.Subject;
import com.vesna1010.college.services.ProfessorService;
import com.vesna1010.college.services.StudyProgramService;
import com.vesna1010.college.services.SubjectService;

public class SubjectsControllerTest extends BaseControllerTest {

	@MockBean
	private SubjectService subjectService;
	@MockBean
	private StudyProgramService studyProgramService;
	@MockBean
	private ProfessorService professorService;
	
	@Test
	@WithAnonymousUser
	public void renderPageWithSubjectsWithAnonymousUserTest() throws Exception {
		renderPageWithSubjectsNotLoggedIn();
	}
	
	private void renderPageWithSubjectsNotLoggedIn() throws Exception {
		mockMvc.perform(get("/subjects"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithSubjectsWithAuthorityProfessorTest() throws Exception {
		renderPageWithSubjects();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithSubjectsWithAuthorityUserTest() throws Exception {
		renderPageWithSubjects();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithSubjectsWithAuthorityAdminTest() throws Exception {
		renderPageWithSubjects();
	}
	
	private void renderPageWithSubjects() throws Exception {
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program");
		List<Subject> subjects = Arrays.asList(new Subject(1L, "Subject A", studyProgram),
				new Subject(2L, "Subject B", studyProgram));

		when(subjectService.findAllSubjects(PAGEABLE)).thenReturn(new PageImpl<Subject>(subjects));
		
		mockMvc.perform(get("/subjects"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
		       .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
		       .andExpect(view().name("subjects/page"));
		
		verify(subjectService, times(1)).findAllSubjects(PAGEABLE);
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithSubjectsByStudyProgramIdWithAnonymousUserTest() throws Exception {
		renderPageWithSubjectsByStudyProgramIdNotLoggedIn();
	}
	
	private void renderPageWithSubjectsByStudyProgramIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/subjects")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithSubjectsByStudyProgramIdWithAuthorityProfessorTest() throws Exception {
		renderPageWithSubjectsByStudyProgramId();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithSubjectsByStudyProgramIdWithAuthorityUserTest() throws Exception {
		renderPageWithSubjectsByStudyProgramId();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithSubjectsByStudyProgramIdWithAuthorityAdminTest() throws Exception {
		renderPageWithSubjectsByStudyProgramId();
	}
	
	private void renderPageWithSubjectsByStudyProgramId() throws Exception {
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program");
		List<Subject> subjects = Arrays.asList(new Subject(1L, "Subject A", studyProgram),
				new Subject(2L, "Subject B", studyProgram));

		when(subjectService.findAllSubjectsByStudyProgramId(1L, PAGEABLE)).thenReturn(new PageImpl<Subject>(subjects));

		mockMvc.perform(
				get("/subjects")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
		       .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
		       .andExpect(view().name("subjects/page"));
		
		verify(subjectService, times(1)).findAllSubjectsByStudyProgramId(1L, PAGEABLE);
	}
	
	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}
	
	private void renderEmptyFormNotLoggedIn() throws Exception {
		mockMvc.perform(get("/subjects/form"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(get("/subjects/form"))
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
		List<Professor> professors = Arrays.asList(new Professor(1L, "Professor A"), new Professor(2L, "Professor B"));
		List<StudyProgram> studyPrograms = Arrays.asList(new StudyProgram(1L, "Study Program A"),
				new StudyProgram(2L, "Study Program B"));

		when(professorService.findAllProfessors(SORT)).thenReturn(professors);
		when(studyProgramService.findAllStudyPrograms(SORT)).thenReturn(studyPrograms);

		mockMvc.perform(get("/subjects/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("subject", is(new Subject())))
		       .andExpect(model().attribute("professors", hasSize(2)))
		       .andExpect(model().attribute("studyPrograms", hasSize(2)))
		       .andExpect(view().name("subjects/form"));
		
		verify(professorService, times(1)).findAllProfessors(SORT);
		verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
	}
	
	@Test
	@WithAnonymousUser
	public void saveSubjectWithAnonymousUserTest() throws Exception {
		saveSubjectNotLoggedIn();
	}
	
	private void saveSubjectNotLoggedIn() throws Exception {
		mockMvc.perform(
				post("/subjects/save")
				.param("id", "1")
				.param("name", "Subject")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveSubjectWithAuthorityProfessorTest() throws Exception {
		saveSubjectNotAuthorized();
	}
	
	private void saveSubjectNotAuthorized() throws Exception {
		mockMvc.perform(
				post("/subjects/save")
				.param("id", "1")
				.param("name", "Subject")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveSubjectWithAuthorityUserTest() throws Exception {
		saveSubject();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveSubjectWithAuthorityAdminTest() throws Exception {
		saveSubject();
	}
	
	private void saveSubject() throws Exception {
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program");
		Professor professor = new Professor(1L, "Professor");
		Subject subject = new Subject(1L, "Subject", studyProgram, new HashSet<Professor>(Arrays.asList(professor)));

		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(subjectService.saveSubject(subject)).thenReturn(subject);
	
		mockMvc.perform(
				post("/subjects/save")
				.param("id", "1")
				.param("name", "Subject")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/subjects/form"));
		
	    verify(studyProgramService, times(1)).findStudyProgramById(1L);
	    verify(professorService, times(1)).findProfessorById(1L);
	    verify(subjectService, times(1)).saveSubject(subject);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveSubjectInvalidFormTest() throws Exception {
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program");
		Professor professor = new Professor(1L, "Professor");
		Subject subject = new Subject(1L, "Subject ??", studyProgram, new HashSet<Professor>(Arrays.asList(professor)));

		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram);
		when(professorService.findProfessorById(1L)).thenReturn(professor);
		when(subjectService.saveSubject(subject)).thenReturn(subject);
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor));
		when(studyProgramService.findAllStudyPrograms(SORT)).thenReturn(Arrays.asList(studyProgram));
	
		mockMvc.perform(
				post("/subjects/save")
				.param("id", "1")
				.param("name", "Subject ??")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("subject", "name"))
		       .andExpect(model().attribute("subject", is(subject)))
		       .andExpect(model().attribute("professors", hasSize(1)))
		       .andExpect(model().attribute("studyPrograms", hasSize(1)))
		       .andExpect(view().name("subjects/form"));
		
	    verify(studyProgramService, times(1)).findStudyProgramById(1L);
	    verify(professorService, times(2)).findProfessorById(1L);
	    verify(subjectService, never()).saveSubject(subject);
	    verify(professorService, times(1)).findAllProfessors(SORT);
	    verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
	}
	
	@Test
	@WithAnonymousUser
	public void rederFormWithSubjectWithAnonymousUserTest() throws Exception {
		rederFormWithSubjectNotLoggedIn();
	}
	
	private void rederFormWithSubjectNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithSubjectWithAuthorityProfessorTest() throws Exception {
		rederFormWithSubjectNotAuthorized();
	}
	
	private void rederFormWithSubjectNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void rederFormWithSubjectWithAuthorityUserTest() throws Exception {
		rederFormWithSubject();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederFormWithSubjectWithAuthorityAdminTest() throws Exception {
		rederFormWithSubject();
	}
	
	private void rederFormWithSubject() throws Exception {
		Subject subject = new Subject(1L, "Subject");
		List<Professor> professors = Arrays.asList(new Professor(1L, "Professor"));
		List<StudyProgram> studyPrograms = Arrays.asList(new StudyProgram(1L, "Study Program"));
		
		when(subjectService.findSubjectById(1L)).thenReturn(subject);
		when(professorService.findAllProfessors(SORT)).thenReturn(professors);
		when(studyProgramService.findAllStudyPrograms(SORT)).thenReturn(studyPrograms);
	
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("subject", hasProperty("name", is("Subject"))))
		       .andExpect(model().attribute("professors", hasSize(1)))
		       .andExpect(model().attribute("studyPrograms", hasSize(1)))
		       .andExpect(view().name("subjects/form"));
		
		verify(professorService, times(1)).findAllProfessors(SORT);
		verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
		verify(subjectService, times(1)).findSubjectById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void deleteSubjectByIdWithAnonymousUserTest() throws Exception {
		deleteSubjectByIdNotLoggedIn();
	}
	
	private void deleteSubjectByIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/subjects/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteSubjectByIdWithAuthorityProfessorTest() throws Exception {
		deleteSubjectByIdNotAuthorized();
	}
	
	private void deleteSubjectByIdNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/subjects/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteSubjectByIdWithAuthorityUserTest() throws Exception {
		deleteSubjectById();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteSubjectByIdWithAuthorityAdminTest() throws Exception {
		deleteSubjectById();
	}
	
	private void deleteSubjectById() throws Exception {
		doNothing().when(subjectService).deleteSubjectById(1L);
		
		mockMvc.perform(
				get("/subjects/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/subjects"));
	
		verify(subjectService, times(1)).deleteSubjectById(1L);
	}
}
