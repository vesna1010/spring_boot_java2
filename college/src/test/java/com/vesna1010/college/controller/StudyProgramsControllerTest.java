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
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.controller.BaseControllerTest;
import com.vesna1010.college.models.StudyProgram;
import com.vesna1010.college.services.DepartmentService;
import com.vesna1010.college.services.StudyProgramService;

public class StudyProgramsControllerTest extends BaseControllerTest {

	@MockBean
	private StudyProgramService studyProgramService;
	@MockBean
	private DepartmentService departmentService;
	
	@Test
	@WithAnonymousUser
	public void renderPageWithStudyProgramsWithAnonymousUserTest() throws Exception {
		renderPageWithStudyProgramsNotLoggedIn();
	}
	
	private void renderPageWithStudyProgramsNotLoggedIn() throws Exception {
		when(studyProgramService.findAllStudyPrograms(PAGEABLE)).thenReturn(
				new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram2, studyProgram3)));
	
		mockMvc.perform(get("/study_programs"))
	           .andExpect(status().is3xxRedirection())
	           .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studyProgramService, times(0)).findAllStudyPrograms(PAGEABLE);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithStudyProgramsWithAuthorityProfessorTest() throws Exception {
		renderPageWithStudyPrograms();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithStudyProgramsWithAuthorityUserTest() throws Exception {
		renderPageWithStudyPrograms();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithStudyProgramsWithAuthorityAdminTest() throws Exception {
		renderPageWithStudyPrograms();
	}
	
	private void renderPageWithStudyPrograms() throws Exception {
		when(studyProgramService.findAllStudyPrograms(PAGEABLE)).thenReturn(
				new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram2, studyProgram3)));
		
		mockMvc.perform(get("/study_programs"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(3))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("study_programs/page"));
		
		verify(studyProgramService, times(1)).findAllStudyPrograms(PAGEABLE);
	}
	
	@Test
	@WithAnonymousUser
	public void renderPageWithStudyProgramsByDepartmentIdWithAnonymousUserTest() throws Exception {
		renderPageWithStudyProgramsByDepartmentIdNotLoggedIn();
	}
	
	private void renderPageWithStudyProgramsByDepartmentIdNotLoggedIn() throws Exception {
		when(studyProgramService.findAllStudyProgramsByDepartmentId(1L, PAGEABLE)).thenReturn(
				new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram3)));
	
		mockMvc.perform(
				get("/study_programs")
				.param("id", "1")
				)
	           .andExpect(status().is3xxRedirection())
	           .andExpect(redirectedUrlPattern("**/login"));
		
		verify(studyProgramService, times(0)).findAllStudyProgramsByDepartmentId(1L, PAGEABLE);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithStudyProgramsByDepartmentIdWithAuthorityProfessorTest() throws Exception {
		renderPageWithStudyProgramsByDepartmentId();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithStudyProgramsByDepartmentIdWithAuthorityUserTest() throws Exception {
		renderPageWithStudyProgramsByDepartmentId();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithStudyProgramsByDepartmentIdWithAuthorityAdminTest() throws Exception {
		renderPageWithStudyProgramsByDepartmentId();
	}
	
	private void renderPageWithStudyProgramsByDepartmentId() throws Exception {
		when(studyProgramService.findAllStudyProgramsByDepartmentId(1L, PAGEABLE))
				.thenReturn(new PageImpl<StudyProgram>(Arrays.asList(studyProgram1, studyProgram3)));

		mockMvc.perform(
				get("/study_programs")
				.param("id", "1")
				)
	           .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("study_programs/page"));;
		
		verify(studyProgramService, times(1)).findAllStudyProgramsByDepartmentId(1L, PAGEABLE);
	}
	
	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}
	
	private void renderEmptyFormNotLoggedIn() throws Exception {
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
	
		mockMvc.perform(get("/study_programs/form"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(departmentService, times(0)).findAllDepartments(SORT);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
	
		mockMvc.perform(get("/study_programs/form"))
		       .andExpect(status().isForbidden());
		
		verify(departmentService, times(0)).findAllDepartments(SORT);
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
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
	
		mockMvc.perform(get("/study_programs/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("departments", is(hasSize(2))))
		       .andExpect(model().attribute("studyProgram", is(new StudyProgram())))
               .andExpect(view().name("study_programs/form"));
		
		verify(departmentService, times(1)).findAllDepartments(SORT);
	}
	
	@WithAnonymousUser
	public void saveStudyProgramWithAnonymousUserTest() throws Exception {
		saveStudyProgramNotLoggedIn();
	}
	
	private void saveStudyProgramNotLoggedIn() throws Exception {
		StudyProgram studyProgram = new StudyProgram("Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department1);
		
		when(departmentService.findDepartmentById(1L)).thenReturn(department1);
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(
				new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1));	
		
		mockMvc.perform(
				post("/study_programs/save")
				.param("name", "Study Program")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(departmentService, times(0)).findDepartmentById(1L);
		verify(studyProgramService, times(0)).saveStudyProgram(studyProgram);	
	}
	
	@WithMockUser(authorities = "PROFESSOR")
	public void saveStudyProgramWithAuthorityProfessorTest() throws Exception {
		saveStudyProgramNotAuthorized();
	}
	
	private void saveStudyProgramNotAuthorized() throws Exception {
		StudyProgram studyProgram = new StudyProgram("Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department1);
		
		when(departmentService.findDepartmentById(1L)).thenReturn(department1);
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(
				new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1));	
			
		mockMvc.perform(
				post("/study_programs/save")
				.param("name", "Study Program")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(departmentService, times(0)).findDepartmentById(1L);
		verify(studyProgramService, times(0)).saveStudyProgram(studyProgram);	
	}
	
	@WithMockUser(authorities = "USER")
	public void saveStudyProgramWithAuthorityUserTest() throws Exception {
		saveStudyProgram();
	}
	
	@WithMockUser(authorities = "ADMIN")
	public void saveStudyProgramWithAuthorityAdminTest() throws Exception {
		saveStudyProgram();
	}
	
	private void saveStudyProgram() throws Exception {
		StudyProgram studyProgram = new StudyProgram("Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department1);
		
		when(departmentService.findDepartmentById(1L)).thenReturn(department1);
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(
				new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1));	
		
		mockMvc.perform(
				post("/study_programs/save")
				.param("name", "Study Program")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/study_programs/form"));
		
		verify(studyProgramService, times(1)).saveStudyProgram(studyProgram);	
		verify(departmentService, times(1)).findDepartmentById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveStudyProgramInvalidFormTest() throws Exception {
		StudyProgram studyProgram = new StudyProgram("Study Program ??", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department1);
		
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(
				new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3, department1));	
		when(departmentService.findDepartmentById(1L)).thenReturn(department1);
		
		mockMvc.perform(
				post("/study_programs/save")
				.param("name", "Study Program ??")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("studyProgram", "name"))
		       .andExpect(model().attribute("studyProgram", hasProperty("id", is(nullValue()))))
		       .andExpect(model().attribute("departments", hasSize(2)))
		       .andExpect(view().name("study_programs/form"));
		
		verify(departmentService, times(1)).findAllDepartments(SORT);
		verify(studyProgramService, times(0)).saveStudyProgram(studyProgram);
		verify(departmentService, times(1)).findDepartmentById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void rederFormWithStudyProgramWithAnonymousUserTest() throws Exception {
		rederFormWithStudyProgramNotLoggedIn();
	}
	
	private void rederFormWithStudyProgramNotLoggedIn() throws Exception {
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(departmentService, times(0)).findAllDepartments(SORT);
		verify(studyProgramService, times(0)).findStudyProgramById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithStudyProgramWithAuthorityProfessorTest() throws Exception {
		rederFormWithStudyProgramNotAuthorized();
	}
	
	private void rederFormWithStudyProgramNotAuthorized() throws Exception {
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
		
		verify(departmentService, times(0)).findAllDepartments(SORT);
		verify(studyProgramService, times(0)).findStudyProgramById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void rederFormWithStudyProgramWithAuthorityUserTest() throws Exception {
		rederFormWithStudyProgram();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederFormWithStudyProgramWithAuthorityAdminTest() throws Exception {
		rederFormWithStudyProgram();
	}
	
	private void rederFormWithStudyProgram() throws Exception {
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department2, department1));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("studyProgram", hasProperty("name", is("Study Program B"))))
               .andExpect(model().attribute("departments", hasSize(2)))
               .andExpect(view().name("study_programs/form"));
		
		verify(departmentService, times(1)).findAllDepartments(SORT);
		verify(studyProgramService, times(1)).findStudyProgramById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void rederStudyProgramDetailsWithAnonymousUserTest() throws Exception {
		rederStudyProgramDetails();
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederStudyProgramDetailsWithAuthorityProfessorTest() throws Exception {
		rederStudyProgramDetails();
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void rederStudyProgramDetailsWithAuthorityUserTest() throws Exception {
		rederStudyProgramDetails();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederStudyProgramDetailsWithAuthorityAdminTest() throws Exception {
		rederStudyProgramDetails();
	}
	
	private void rederStudyProgramDetails() throws Exception {
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram1);
		
		mockMvc.perform(
				get("/study_programs/details")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("studyProgram", hasProperty("name", is("Study Program B"))))
               .andExpect(view().name("study_programs/details"));;
		
		verify(studyProgramService, times(1)).findStudyProgramById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void deleteStudyProgramByIdWithAnonymousUserTest() throws Exception {
		deleteStudyProgramByIdNotLoggedIn();
	}
	
	private void  deleteStudyProgramByIdNotLoggedIn() throws Exception {
		doNothing().when(studyProgramService). deleteStudyProgramById(1L);
		
		mockMvc.perform(
				get("/study_programs/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(studyProgramService, times(0)). deleteStudyProgramById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteStudyProgramByIdWithAuthorityProfessorTest() throws Exception {
		deleteStudyProgramByIdNotAuthorized();
	}
	
	private void deleteStudyProgramByIdNotAuthorized() throws Exception {
		doNothing().when(studyProgramService).deleteStudyProgramById(1L);
		
		mockMvc.perform(
				get("/study_programs/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(studyProgramService, times(0)).deleteStudyProgramById(1L);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void deleteStudyProgramByIdWithAuthorityUserTest() throws Exception {
		deleteStudyProgramById();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteStudyProgramByIdWithAuthorityAdminTest() throws Exception {
		deleteStudyProgramById();
	}
	
	private void deleteStudyProgramById() throws Exception {
		doNothing().when(studyProgramService).deleteStudyProgramById(1L);
		
		mockMvc.perform(
				get("/study_programs/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/study_programs"));
	
		verify(studyProgramService, times(1)).deleteStudyProgramById(1L);
	}
	
}
