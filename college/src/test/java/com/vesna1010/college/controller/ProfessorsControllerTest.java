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
import com.vesna1010.college.enums.Gender;
import com.vesna1010.college.models.Professor;
import com.vesna1010.college.services.ProfessorService;

public class ProfessorsControllerTest extends BaseControllerTest {

	@MockBean
	private ProfessorService service;

	@Test
	@WithAnonymousUser
	public void renderPageWithProfessorsWithAnonymousUserTest() throws Exception {
		renderPageWithProfessorsNotLoggedIn();
	}

	private void renderPageWithProfessorsNotLoggedIn() throws Exception {
		when(service.findAllProfessors(PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2, professor3)));

		mockMvc.perform(get("/professors"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(service, times(0)).findAllProfessors(PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithProfessorsWithAuthorityProfessorTest() throws Exception {
		renderPageWithProfessorsNotAuthorized();
	}
	
	private void renderPageWithProfessorsNotAuthorized() throws Exception {
		when(service.findAllProfessors(PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2, professor3)));

		mockMvc.perform(get("/professors"))
               .andExpect(status().isForbidden());
	
		verify(service, times(0)).findAllProfessors(PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithProfessorsWithAuthorityUserTest() throws Exception {
		renderPageWithProfessors();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithProfessorsWithAuthorityAdminTest() throws Exception {
		renderPageWithProfessors();
	}
	
	private void renderPageWithProfessors() throws Exception {
		when(service.findAllProfessors(PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2, professor3)));

		mockMvc.perform(get("/professors"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("page", hasProperty("content", hasSize(3))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("professors/page"));
	
		verify(service, times(1)).findAllProfessors(PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderPageWithProfessorsByStudyProgrammeIdWithAnonymousUserTest() throws Exception {
		renderPageWithProfessorsByStudyProgrammeIdNotLoggedIn();
	}

	private void renderPageWithProfessorsByStudyProgrammeIdNotLoggedIn() throws Exception {
		when(service.findAllProfessorsByStudyProgramId(1L, PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2)));

		mockMvc.perform(
				get("/professors")
				.param("id", "1")
				)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(service, times(0)).findAllProfessorsByStudyProgramId(1L, PAGEABLE);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderPageWithProfessorsByStudyProgrammeIdWithAuthorityProfessorTest() throws Exception {
		renderPageWithProfessorsByStudyProgrammeIdNotAuthorized();
	}
	
	private void renderPageWithProfessorsByStudyProgrammeIdNotAuthorized() throws Exception {
		when(service.findAllProfessorsByStudyProgramId(1L, PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2)));

		mockMvc.perform(
				get("/professors")
				.param("id", "1")
				)
               .andExpect(status().isForbidden());
	
		verify(service, times(0)).findAllProfessorsByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void renderPageWithProfessorsByStudyProgrammeIdWithAuthorityUserTest() throws Exception {
		renderPageWithProfessorsByStudyProgrammeId();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWithProfessorsByStudyProgrammeIdWithAuthorityAdminTest() throws Exception {
		renderPageWithProfessorsByStudyProgrammeId();
	}
	
	private void renderPageWithProfessorsByStudyProgrammeId() throws Exception {
		when(service.findAllProfessorsByStudyProgramId(1L, PAGEABLE))
				.thenReturn(new PageImpl<Professor>(Arrays.asList(professor1, professor2)));

		mockMvc.perform(
				get("/professors")
				.param("id", "1")
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
               .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
               .andExpect(view().name("professors/page"));
	
		verify(service, times(1)).findAllProfessorsByStudyProgramId(1L, PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLoggedIn();
	}
	
	private void renderEmptyFormNotLoggedIn() throws Exception {
		mockMvc.perform(get("/professors/form"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void renderEmptyFormWithAuthorityProfessorTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}
	
	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(get("/professors/form"))
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
		mockMvc.perform(get("/professors/form"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("professor", is(new Professor())))
               .andExpect(view().name("professors/form"));
	}
	
	@Test
	@WithAnonymousUser
	public void saveProfessorWithAnonymousUserTest() throws Exception {
		saveProfessorNotLoggedIn();
	}
	
	private void saveProfessorNotLoggedIn() throws Exception {
		Professor professor = new Professor("Professor", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		when(service.saveProfessor(professor))
				.thenReturn(new Professor(4L, "Professor", "Parent", LocalDate.of(1995, Month.JANUARY, 6),
						"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title"));
	
		mockMvc.perform(
				post("/professors/save")
				.param("name", "Professor")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1985")
				.param("email", "profesor@gmail.com")
				.param("telephone", "065 123 338")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", professor.getPhoto().toString())
				.param("title", "Title")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(service, times(0)).saveProfessor(professor);
	}
	
	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void saveProfessorWithAuthorityProfessorTest() throws Exception {
		saveProfessorNotAuthorized();
	}
	
	private void saveProfessorNotAuthorized() throws Exception {
		Professor professor = new Professor("Professor", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		when(service.saveProfessor(professor))
				.thenReturn(new Professor(4L, "Professor", "Parent", LocalDate.of(1995, Month.JANUARY, 6),
						"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title"));
	
		mockMvc.perform(
				post("/professors/save")
				.param("name", "Professor")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1985")
				.param("email", "profesor@gmail.com")
				.param("telephone", "065 123 338")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", professor.getPhoto().toString())
				.param("title", "Title")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
		
		verify(service, times(0)).saveProfessor(professor);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void saveProfessorWithAuthorityUserTest() throws Exception {
		saveProfessor();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveProfessorWithAuthorityAdminTest() throws Exception {
		saveProfessor();
	}
	
	private void saveProfessor() throws Exception {
		Professor professor = new Professor("Professor", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		when(service.saveProfessor(professor))
				.thenReturn(new Professor(4L, "Professor", "Parent", LocalDate.of(1995, Month.JANUARY, 6),
						"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title"));
	
		mockMvc.perform(
				post("/professors/save")
				.param("name", "Professor")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1985")
				.param("email", "profesor@gmail.com")
				.param("telephone", "065 123 338")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", professor.getPhoto().toString())
				.param("title", "Title")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/professors/form"));
		
		verify(service, times(1)).saveProfessor(professor);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveProfessorInvalidFormTest() throws Exception {
		Professor professor = new Professor("Professor ??", "Parent", LocalDate.of(1985, Month.JANUARY, 6),
				"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title");

		when(service.saveProfessor(professor))
				.thenReturn(new Professor(4L, "Professor ??", "Parent", LocalDate.of(1995, Month.JANUARY, 6),
						"profesor@gmail.com", "065 123 338", Gender.MALE, "Address", getPhoto(), "Title"));
		
		mockMvc.perform(
				post("/professors/save")
				.param("name", "Professor ??")
				.param("parent", "Parent")
				.param("birthDate", "06.01.1985")
				.param("email", "profesor@gmail.com")
				.param("telephone", "065 123 338")
				.param("gender", "MALE")
				.param("address", "Address")
				.param("photo", professor.getPhoto().toString())
				.param("title", "Title")
				.with(csrf())
				)
		       .andExpect(status().isOk())
               .andExpect(model().attributeHasFieldErrors("professor", "name"))
               .andExpect(model().attribute("professor", hasProperty("id", is(nullValue()))))
               .andExpect(view().name("professors/form"));
		
		verify(service, times(0)).saveProfessor(professor);
	}

	@Test
	@WithAnonymousUser
	public void rederFormWithProfessorSubjectWithAnonymousUserTest() throws Exception {
		rederFormWithProfessorNotLoggedIn();
	}
	
	private void rederFormWithProfessorNotLoggedIn() throws Exception {
		when(service.findProfessorById(1L)).thenReturn(professor1);
		
		mockMvc.perform(
				get("/professors/edit")
				.param("id","1")
				)
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
		
		verify(service, times(0)).findProfessorById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void rederFormWithProfessorWithAuthorityProfessorTest() throws Exception {
		rederFormWithProfessorNotAuthorized();
	}
	
	private void rederFormWithProfessorNotAuthorized() throws Exception {
		when(service.findProfessorById(1L)).thenReturn(professor1);
		
		mockMvc.perform(
				get("/professors/edit")
				.param("id","1")
				)
               .andExpect(status().isForbidden());
		
		verify(service, times(0)).findProfessorById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void rederFormWithProfessorWithAuthorityUserTest() throws Exception {
		rederFormWithProfessor();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void rederFormWithProfessorWithAuthorityAdminTest() throws Exception {
		rederFormWithProfessor();
	}
	
	private void rederFormWithProfessor() throws Exception {
		when(service.findProfessorById(1L)).thenReturn(professor1);
		
		mockMvc.perform(
				get("/professors/edit")
				.param("id","1")
				)
		       .andExpect(status().isOk())
               .andExpect(model().attribute("professor", hasProperty("name", is("Professor B"))))
               .andExpect(view().name("professors/form"));
		
		verify(service, times(1)).findProfessorById(1L);
	}

	@Test
	@WithAnonymousUser
	public void deleteProfessorByIdWithAnonymousUserTest() throws Exception {
		deleteProfessorByIdNotLoggedIn();
	}
	
	private void deleteProfessorByIdNotLoggedIn() throws Exception {
		doNothing().when(service).deleteProfessorById(1L);
		
		mockMvc.perform(
				get("/professors/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
	
		verify(service, times(0)).deleteProfessorById(1L);
	}

	@Test
	@WithMockUser(authorities = "PROFESSOR")
	public void deleteProfessorByIdWithAuthorityProfessorTest() throws Exception {
		deleteProfessorByIdNotAuthorized();
	}
	
	private void deleteProfessorByIdNotAuthorized() throws Exception {
		doNothing().when(service).deleteProfessorById(1L);
		
		mockMvc.perform(
				get("/professors/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	
		verify(service, times(0)).deleteProfessorById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteProfessorByIdWithAuthorityUserTest() throws Exception {
		deleteProfessorById();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteProfessorByIdWithAuthorityAdminTest() throws Exception {
		deleteProfessorById();
	}
	
	private void deleteProfessorById() throws Exception {
		doNothing().when(service).deleteProfessorById(1L);
		
		mockMvc.perform(
				get("/professors/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/professors"));
	
		verify(service, times(1)).deleteProfessorById(1L);
	}

}
