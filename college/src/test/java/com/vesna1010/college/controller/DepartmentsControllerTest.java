package com.vesna1010.college.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
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
import com.vesna1010.college.models.Department;
import com.vesna1010.college.services.DepartmentService;

public class DepartmentsControllerTest extends BaseControllerTest {

	@MockBean
	private DepartmentService service;

	@Test
	@WithAnonymousUser
	public void renderPageWithDepartmentsWithAnonymousUserTest() throws Exception {
		renderPageWithDepartmentsNotLoggedIn();
	}
	
	private void renderPageWithDepartmentsNotLoggedIn() throws Exception {
		when(service.findAllDepartments(PAGEABLE))
				.thenReturn(new PageImpl<Department>(Arrays.asList(department1, department2)));
		
		mockMvc.perform(get("/departments"))
	               .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(service, times(0)).findAllDepartments(PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithDepartmentsWithAuthorityProfessorTest() throws Exception {
		renderPageWithDepartments();
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithDepartmentsWithAuthorityUserTest() throws Exception {
		renderPageWithDepartments();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithDepartmentsWithAuthorityAdminTest() throws Exception {
		renderPageWithDepartments();
	}
	
	private void renderPageWithDepartments() throws Exception {
		when(service.findAllDepartments(PAGEABLE))
				.thenReturn(new PageImpl<Department>(Arrays.asList(department1, department2)));
	
		mockMvc.perform(get("/departments"))
	               .andExpect(status().isOk())
	               .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
	               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
	               .andExpect(view().name("departments/page"));
		
		verify(service, times(1)).findAllDepartments(PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}
	
	private void renderEmptyFormNotLoggedIn() throws Exception {
		mockMvc.perform(get("/departments/form"))
                       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}

	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(get("/departments/form"))
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
		mockMvc.perform(get("/departments/form"))
		       .andExpect(status().isOk())
                       .andExpect(model().attribute("department", is(new Department())))
                       .andExpect(view().name("departments/form"));
	}
	
	@Test
	@WithAnonymousUser
	public void saveDepartmentWithAnonymousUserTest() throws Exception {
		saveDepartmentNotLoggedIn();
	}
	
	private void saveDepartmentNotLoggedIn() throws Exception {
		Department department = new Department("Department", LocalDate.of(2019, Month.JANUARY, 1));
		
		when(service.saveDepartment(department))
				.thenReturn(new Department(1L, "Department", LocalDate.of(2019, Month.JANUARY, 1)));
		
		mockMvc.perform(
				post("/departments/save")
				.param("name", "Department")
				.param("createdOn", "01.01.2019")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(service, times(0)).saveDepartment(department);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveDepartmentWithAuthorityProfessorTest() throws Exception {
		saveDepartmentNotAuthorized();
	}
	
	private void saveDepartmentNotAuthorized() throws Exception {
		Department department = new Department("Department", LocalDate.of(2019, Month.JANUARY, 1));
		
		when(service.saveDepartment(department))
				.thenReturn(new Department(1L, "Department", LocalDate.of(2019, Month.JANUARY, 1)));
		
		mockMvc.perform(
				post("/departments/save")
				.param("name", "Department")
				.param("createdOn", "01.01.2019")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
		
		verify(service, times(0)).saveDepartment(department);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveDepartmentWithAuthorityUserTest() throws Exception {
		saveDepartment();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveDepartmentWithAuthorityAdminTest() throws Exception {
		saveDepartment();
	}

	private void saveDepartment() throws Exception {
		Department department = new Department("Department", LocalDate.of(2019, Month.JANUARY, 1));
		
		when(service.saveDepartment(department))
				.thenReturn(new Department(1L, "Department", LocalDate.of(2019, Month.JANUARY, 1)));
		
		mockMvc.perform(
				post("/departments/save")
				.param("name", "Department")
				.param("createdOn", "01.01.2019")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/departments/form"));
		
		verify(service, times(1)).saveDepartment(department);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveDepartmentInvalidFormTest() throws Exception {
		Department department = new Department("Department ??", LocalDate.of(2019, Month.JANUARY, 1));
		
		when(service.saveDepartment(department))
				.thenReturn(new Department(1L, "Department ??", LocalDate.of(2019, Month.JANUARY, 1)));
		
		mockMvc.perform(
				post("/departments/save")
				.param("name", "Department ??")
				.param("createdOn", "01.01.2019")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("department", "name"))
		       .andExpect(model().attribute("department", hasProperty("id", is(nullValue()))))
		       .andExpect(view().name("departments/form"));
		
		verify(service, times(0)).saveDepartment(department);
	}

	@Test
	@WithAnonymousUser
	public void renderFormWithDepartmentWithAnonymousUserTest() throws Exception {
		renderFormWithDepartmentNotLoggedIn();
	}
	
	private void renderFormWithDepartmentNotLoggedIn() throws Exception {
		when(service.findDepartmentById(1L)).thenReturn(department1);
		
		mockMvc.perform(
				get("/departments/edit")
				.param("id", "1")
				)
                       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
		
		verify(service, times(0)).findDepartmentById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderFormWithDepartmentWithAuthorityProfessorTest() throws Exception {
		renderFormWithDepartmentNotAuthorized();
	}
	
	private void renderFormWithDepartmentNotAuthorized() throws Exception {
		when(service.findDepartmentById(1L)).thenReturn(department1);
		
		mockMvc.perform(
				get("/departments/edit")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
		
		verify(service, times(0)).findDepartmentById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderFormWithDepartmentWithAuthorityUserTest() throws Exception {
		renderFormWithDepartment();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderFormWithDepartmentWithAuthorityAdminTest() throws Exception {
		renderFormWithDepartment();
	}
	
	private void renderFormWithDepartment() throws Exception {
		when(service.findDepartmentById(1L)).thenReturn(department1);
		
		mockMvc.perform(
				get("/departments/edit")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
                       .andExpect(model().attribute("department", hasProperty("name", is("Department B"))))
                       .andExpect(view().name("departments/form"));
		
		verify(service, times(1)).findDepartmentById(1L);
	}

	@Test
	@WithAnonymousUser
	public void deleteDepartmentByIdWithAnonymousUserTest() throws Exception {
		deleteDepartmentByIdNotLoggedIn();
	}
	
	private void deleteDepartmentByIdNotLoggedIn() throws Exception {
		doNothing().when(service).deleteDepartmentById(1L);
		
		mockMvc.perform(
				get("/departments/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrlPattern("**/login"));
	
		verify(service, times(0)).deleteDepartmentById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteDepartmentByIdWithAuthorityProfessorTest() throws Exception {
		deleteDepartmentByIdNotAuthorized();
	}
	
	private void deleteDepartmentByIdNotAuthorized() throws Exception {
		doNothing().when(service).deleteDepartmentById(1L);
		
		mockMvc.perform(
				get("/departments/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(service, times(0)).deleteDepartmentById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteDepartmentByIdWithAuthorityUserTest() throws Exception {
		deleteDepartmentById();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteDepartmentByIdWithAuthorityAdminTest() throws Exception {
		deleteDepartmentById();
	}

	private void deleteDepartmentById() throws Exception {
		doNothing().when(service).deleteDepartmentById(1L);
		
		mockMvc.perform(
				get("/departments/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
                       .andExpect(redirectedUrl("/departments"));
	
		verify(service, times(1)).deleteDepartmentById(1L);
	}
	
}
