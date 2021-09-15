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
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.models.Department;
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
		mockMvc.perform(get("/study_programs"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
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
		Department department = new Department(1L, "Department");
		List<StudyProgram> studyPrograms = Arrays.asList(new StudyProgram(1L, "Study Program A", department),
				new StudyProgram(2L, "Study Program B", department));

		when(studyProgramService.findAllStudyPrograms(PAGEABLE)).thenReturn(new PageImpl<StudyProgram>(studyPrograms));

		mockMvc.perform(get("/study_programs"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
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
		mockMvc.perform(
				get("/study_programs")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
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
		Department department = new Department(1L, "Department");
		List<StudyProgram> studyPrograms = Arrays.asList(new StudyProgram(1L, "Study Program A", department),
				new StudyProgram(2L, "Study Program B", department));

		when(studyProgramService.findAllStudyProgramsByDepartmentId(1L, PAGEABLE))
				.thenReturn(new PageImpl<StudyProgram>(studyPrograms));

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
		mockMvc.perform(get("/study_programs/form"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(get("/study_programs/form"))
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
		List<Department> departments = Arrays.asList(new Department(1L, "Department A"),
				new Department(2L, "Department B"));

		when(departmentService.findAllDepartments(SORT)).thenReturn(departments);

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
		mockMvc.perform(
				post("/study_programs/save")
				.param("id", "1")
				.param("name", "Study Program")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));	
	}
	
	@WithMockUser(authorities = "PROFESSOR")
	public void saveStudyProgramWithAuthorityProfessorTest() throws Exception {
		saveStudyProgramNotAuthorized();
	}
	
	private void saveStudyProgramNotAuthorized() throws Exception {	
		mockMvc.perform(
				post("/study_programs/save")
				.param("id", "1")
				.param("name", "Study Program")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());;	
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
		Department department = new Department(1L, "Department");
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department);
	
		when(departmentService.findDepartmentById(1L)).thenReturn(department);
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(studyProgram);	
		
		mockMvc.perform(
				post("/study_programs/save")
				.param("id", "1")
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
		Department department = new Department(1L, "Department");
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department);
		
		when(departmentService.findDepartmentById(1L)).thenReturn(department);
		when(studyProgramService.saveStudyProgram(studyProgram)).thenReturn(studyProgram);
		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department));
		
		mockMvc.perform(
				post("/study_programs/save")
				.param("id", "1")
				.param("name", "Study Program ??")
				.param("createdOn", "01.01.2019")
				.param("duration", "3")
				.param("department", "1")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("studyProgram", "name"))
		       .andExpect(model().attribute("studyProgram", is(studyProgram)))
		       .andExpect(model().attribute("departments", hasSize(1)))
		       .andExpect(view().name("study_programs/form"));
		
		verify(departmentService, times(1)).findDepartmentById(1L);
		verify(studyProgramService, never()).saveStudyProgram(studyProgram);
		verify(departmentService, times(1)).findAllDepartments(SORT);
	}
	
	@Test
	@WithAnonymousUser
	public void rederFormWithStudyProgramWithAnonymousUserTest() throws Exception {
		rederFormWithStudyProgramNotLoggedIn();
	}
	
	private void rederFormWithStudyProgramNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithStudyProgramWithAuthorityProfessorTest() throws Exception {
		rederFormWithStudyProgramNotAuthorized();
	}
	
	private void rederFormWithStudyProgramNotAuthorized() throws Exception {	
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
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
		Department department = new Department(1L, "Department");
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program", LocalDate.of(2018, Month.SEPTEMBER, 1), 3,
				department);

		when(departmentService.findAllDepartments(SORT)).thenReturn(Arrays.asList(department));
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram);
	
		mockMvc.perform(
				get("/study_programs/edit")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("studyProgram", hasProperty("name", is("Study Program"))))
		       .andExpect(model().attribute("departments", hasSize(1)))
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
		StudyProgram studyProgram = new StudyProgram(1L, "Study Program");
		
		when(studyProgramService.findStudyProgramById(1L)).thenReturn(studyProgram);
		
		mockMvc.perform(
				get("/study_programs/details")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("studyProgram", hasProperty("name", is("Study Program"))))
		       .andExpect(view().name("study_programs/details"));;
		
		verify(studyProgramService, times(1)).findStudyProgramById(1L);
	}
	
	@Test
	@WithAnonymousUser
	public void deleteStudyProgramByIdWithAnonymousUserTest() throws Exception {
		deleteStudyProgramByIdNotLoggedIn();
	}
	
	private void  deleteStudyProgramByIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/study_programs/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteStudyProgramByIdWithAuthorityProfessorTest() throws Exception {
		deleteStudyProgramByIdNotAuthorized();
	}
	
	private void deleteStudyProgramByIdNotAuthorized() throws Exception {	
		mockMvc.perform(
				get("/study_programs/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
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
