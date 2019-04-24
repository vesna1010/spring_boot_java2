package com.vesna1010.college.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.models.Professor;
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
		when(subjectService.findAllSubjects(PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(subject1, subject2, subject3, subject4)));
	
		mockMvc.perform(get("/subjects"))
	               .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(subjectService, times(0)).findAllSubjects(PAGEABLE);
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
		when(subjectService.findAllSubjects(PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(subject1, subject2, subject3, subject4)));
	
		mockMvc.perform(get("/subjects"))
		       .andExpect(status().isOk())
	               .andExpect(model().attribute("page", hasProperty("content", hasSize(4))))
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
		when(subjectService.findAllSubjectsByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(subject1, subject3)));
	
		mockMvc.perform(
				get("/subjects")
				.param("id", "1")
				)
	               .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(subjectService, times(0)).findAllSubjectsByStudyProgramId(1L, PAGEABLE);
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
		when(subjectService.findAllSubjectsByStudyProgramId(1L, PAGEABLE)).thenReturn(
				new PageImpl<Subject>(Arrays.asList(subject1, subject3)));
	
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
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/subjects/form"))
                       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(professorService, times(0)).findAllProfessors(SORT);
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/subjects/form"))
		       .andExpect(status().isForbidden());
		
		verify(professorService, times(0)).findAllProfessors(SORT);
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
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
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		
		mockMvc.perform(get("/subjects/form"))
		       .andExpect(status().isOk())
                       .andExpect(model().attribute("subject", is(new Subject())))
                       .andExpect(model().attribute("professors", hasSize(3)))
                       .andExpect(model().attribute("studyPrograms", hasSize(3)))
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
		Subject subject = new Subject("Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1)));

		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(subjectService.saveSubject(subject)).thenReturn(
				new Subject(5L, "Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1))));
	
		mockMvc.perform(
				post("/subjects/save")
				.param("name", "Subject")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studyProgramService, times(0)).findStudyProgramById(1L);
		verify(professorService, times(0)).findProfessorById(1L);
	        verify(subjectService, times(0)).saveSubject(subject);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveSubjectWithAuthorityProfessorTest() throws Exception {
		saveSubjectNotAuthorized();
	}
	
	private void saveSubjectNotAuthorized() throws Exception {
		Subject subject = new Subject("Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1)));

		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(subjectService.saveSubject(subject)).thenReturn(
				new Subject(5L, "Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1))));
	
		mockMvc.perform(
				post("/subjects/save")
				.param("name", "Subject")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
		
		verify(studyProgramService, times(0)).findStudyProgramById(1L);
		verify(professorService, times(0)).findProfessorById(1L);
	        verify(subjectService, times(0)).saveSubject(subject);
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
		Subject subject = new Subject("Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1)));

		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(subjectService.saveSubject(subject)).thenReturn(
				new Subject(5L, "Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1))));
	
		mockMvc.perform(
				post("/subjects/save")
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
		Subject subject = new Subject("Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1)));

		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		when(professorService.findProfessorById(1L)).thenReturn(professor1);
		when(subjectService.saveSubject(subject)).thenReturn(
				new Subject(5L, "Subject", studyProgram1, new HashSet<Professor>(Arrays.asList(professor1))));
		
		mockMvc.perform(
				post("/subjects/save")
				.param("name", "Subject ??")
				.param("studyProgram", "1")
				.param("professors", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
	               .andExpect(model().attributeHasFieldErrors("subject", "name"))
	               .andExpect(model().attribute("subject", hasProperty("id", is(nullValue()))))
	               .andExpect(model().attribute("professors", hasSize(3)))
	               .andExpect(model().attribute("studyPrograms", hasSize(3)))
	               .andExpect(view().name("subjects/form"));
		
		verify(professorService, times(1)).findAllProfessors(SORT);
		verify(studyProgramService, times(1)).findAllStudyPrograms(SORT);
		verify(studyProgramService, times(1)).findStudyProgramById(1L);
		verify(professorService, times(1)).findProfessorById(1L);
	        verify(subjectService, times(0)).saveSubject(subject);
	}
	
	@Test
	@WithAnonymousUser
	public void rederFormWithSubjectWithAnonymousUserTest() throws Exception {
		rederFormWithSubjectNotLoggedIn();
	}
	
	private void rederFormWithSubjectNotLoggedIn() throws Exception {
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
                       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(professorService, times(0)).findAllProfessors(SORT);
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
		verify(subjectService, times(0)).findSubjectById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithSubjectWithAuthorityProfessorTest() throws Exception {
		rederFormWithSubjectNotAuthorized();
	}
	
	private void rederFormWithSubjectNotAuthorized() throws Exception {
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
                       .andExpect(status().isForbidden());
		
		verify(professorService, times(0)).findAllProfessors(SORT);
		verify(studyProgramService, times(0)).findAllStudyPrograms(SORT);
		verify(subjectService, times(0)).findSubjectById(1L);
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
		when(professorService.findAllProfessors(SORT)).thenReturn(Arrays.asList(professor3, professor1, professor2));
		when(studyProgramService.findAllStudyPrograms(SORT))
				.thenReturn(Arrays.asList(studyProgram3, studyProgram1, studyProgram2));
		when(subjectService.findSubjectById(1L)).thenReturn(subject1);
		
		mockMvc.perform(
				get("/subjects/edit")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
                       .andExpect(model().attribute("subject", hasProperty("name", is("Subject B"))))
                       .andExpect(model().attribute("professors", hasSize(3)))
                       .andExpect(model().attribute("studyPrograms", hasSize(3)))
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
		doNothing().when(subjectService).deleteSubjectById(1L);
		
		mockMvc.perform(
				get("/subjects/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	
		verify(subjectService, times(0)).deleteSubjectById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteSubjectByIdWithAuthorityProfessorTest() throws Exception {
		deleteSubjectByIdNotAuthorized();
	}
	
	private void deleteSubjectByIdNotAuthorized() throws Exception {
		doNothing().when(subjectService).deleteSubjectById(1L);
		
		mockMvc.perform(
				get("/subjects/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(subjectService, times(0)).deleteSubjectById(1L);
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
