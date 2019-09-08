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
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.vesna1010.college.enums.Authority;
import com.vesna1010.college.models.User;
import com.vesna1010.college.services.UserService;

public class UsersControllerTest extends BaseControllerTest {

	@MockBean
	private UserService service;

	@Test
	@WithAnonymousUser
	public void renderPageWitUsersWithAnonymousUserTest() throws Exception {
		renderPageWitUsersNotLogged();
	}
	
	private void renderPageWitUsersNotLogged() throws Exception {
		mockMvc.perform(get("/users"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = { "PROFESSOR", "USER" })
	public void renderPageWitUsersWithAuthorityProfessorAndUserTest() throws Exception {
		renderPageWitUsersNotAuthorized();
	}
	
	private void renderPageWitUsersNotAuthorized() throws Exception {
		mockMvc.perform(get("/users"))
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderPageWitUsersWithAuthorityAdminTest() throws Exception {
		renderPageWitUsers();
	}
	
	private void renderPageWitUsers() throws Exception {
		List<User> users = Arrays.asList(new User(1L, "User A"), new User(2L, "User B"));

		when(service.findAllUsers(PAGEABLE)).thenReturn(new PageImpl<User>(users));

		mockMvc.perform(get("/users"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("page", hasProperty("content", hasSize(2))))
		       .andExpect(model().attribute("page", hasProperty("totalPages", is(1))))
		       .andExpect(view().name("users/page"));
		
		verify(service, times(1)).findAllUsers(PAGEABLE);
	}

	@Test
	@WithAnonymousUser
	public void renderEmptyFormWithAnonymousUserTest() throws Exception {
		renderEmptyFormNotLogged();
	}
	
	private void renderEmptyFormNotLogged() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = { "PROFESSOR", "USER" })
	public void renderEmptyFormWithAuthorityProfessorAndUserTest() throws Exception {
		renderEmptyFormNotAuthorized();
	}

	private void renderEmptyFormNotAuthorized() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().isForbidden());
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void renderEmptyFormWithAuthorityAdminTest() throws Exception {
		renderEmptyForm();
	}
	
	private void renderEmptyForm() throws Exception {
		mockMvc.perform(get("/users/form"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("user", is(new User())))
		       .andExpect(view().name("users/form"));
	}
	
	@Test
	@WithAnonymousUser
	public void saveUserWithAnonymousUserTest() throws Exception {
		saveUserNotLoggedIn();
	}
	
	private void saveUserNotLoggedIn() throws Exception {
		mockMvc.perform(
				post("/users/save")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "Password")
				.param("confirmPassword", "Password")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}
	
	@Test
	@WithMockUser(authorities = { "PROFESSOR", "USER" })
	public void saveUserWithAuthorityProfessorAndUserTest() throws Exception {
		saveUserNotAuthorized();
	}
	
	private void saveUserNotAuthorized() throws Exception {
		mockMvc.perform(
				post("/users/save")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "Password")
				.param("confirmPassword", "Password")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveUserValidFormTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.USER);
		
		when(service.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/save")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "Password")
				.param("confirmPassword", "Password")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(model().hasNoErrors())
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/users/form"));
		
		verify(service, times(1)).saveUser(user);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveUserInvalidFormTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "New Password", Authority.USER);
		
		when(service.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/save")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "New Password")
				.param("confirmPassword", "New Password")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("user", "password"))
		       .andExpect(model().attribute("user", is(user)))
		       .andExpect(view().name("users/form"));
		
		verify(service, times(0)).saveUser(user);
	}

	@Test
	@WithAnonymousUser
	public void deleteUserByIdWithAnonymousUserTest() throws Exception {
		deleteUserByIdNotLoggedIn();
	}
	
	private void deleteUserByIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/users/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = { "PROFESSOR", "USER" })
	public void deleteUserByIdWithAuthorityProfessorAndUserTest() throws Exception {
		deleteUserByIdNotAuthorized();
	}
	
	private void deleteUserByIdNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/users/delete")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUserByIdWithAuthorityAdminTest() throws Exception {
		deleteUserById();
	}
	
	private void deleteUserById() throws Exception {
		doNothing().when(service).deleteUserById(1L);
		
		mockMvc.perform(
				get("/users/delete")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/users"));
		
		verify(service, times(1)).deleteUserById(1L);
	}

	@Test
	@WithAnonymousUser
	public void disableUserByIdWithAnonymousUserTest() throws Exception {
		disableUserByIdNotLoggedIn();
	}
	
	private void disableUserByIdNotLoggedIn() throws Exception {
		mockMvc.perform(
				get("/users/disable")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(authorities = { "PROFESSOR", "USER" })
	public void disableUserByIdWithAuthorityProfessorTest() throws Exception {
		disableUserByIdNotAuthorized();
	}
	
	private void disableUserByIdNotAuthorized() throws Exception {
		mockMvc.perform(
				get("/users/disable")
				.param("id", "1")
				)
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void disableUserByIdWithAuthorityAdminTest() throws Exception {
		disableUserById();
	}
	
	private void disableUserById() throws Exception {
		doNothing().when(service).disableUserById(1L);
		
		mockMvc.perform(
				get("/users/disable")
				.param("id", "1")
				)
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrl("/users"));
		
		verify(service, times(1)).disableUserById(1L);
	}

	@Test
	@WithAnonymousUser
	public void renderFormWithUserWithAnonymousUserTest() throws Exception {
		renderFormWithUserNotLoggedIn();
	}
	
	private void renderFormWithUserNotLoggedIn() throws Exception {
		mockMvc.perform(get("/users/edit"))
		       .andExpect(status().is3xxRedirection())
		       .andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	@WithMockUser(username = "user@gmail.com", authorities = "PROFESSOR")
	public void renderFormWithUserWithAuthorityUserAndProfessorTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.PROFESSOR);
		
		when(service.findUserByEmail("user@gmail.com")).thenReturn(user);
		
		mockMvc.perform(get("/users/edit"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("user", hasProperty("name", is("User"))))
		       .andExpect(view().name("users/update-password-form"));
		
		verify(service, times(1)).findUserByEmail("user@gmail.com");
	}

	@Test
	@WithMockUser(username = "user@gmail.com", authorities = "USER")
	public void renderFormWithUserWithAuthorityUserTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.USER);
		
		when(service.findUserByEmail("user@gmail.com")).thenReturn(user);
		
		
		mockMvc.perform(get("/users/edit"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("user", hasProperty("name", is("User"))))
		       .andExpect(view().name("users/update-password-form"));
		
		verify(service, times(1)).findUserByEmail("user@gmail.com");
	}

	@Test
	@WithMockUser(username = "user@gmail.com", authorities = "ADMIN")
	public void renderFormWithUserWithAuthorityAdminTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.ADMIN);
		
		when(service.findUserByEmail("user@gmail.com")).thenReturn(user);
		
		mockMvc.perform(get("/users/edit"))
		       .andExpect(status().isOk())
		       .andExpect(model().attribute("user", hasProperty("name", is("User"))))
		       .andExpect(view().name("users/update-password-form"));
		
		verify(service, times(1)).findUserByEmail("user@gmail.com");
	}

	@Test
	@WithMockUser(username = "user@gmail.com", authorities = "USER")
	public void updatePasswordValidFormTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.USER);
		
		when(service.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/update")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "Password")
				.param("confirmPassword", "Password")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().hasNoErrors())
		       .andExpect(model().attribute("user", hasProperty("email", is("user@gmail.com"))))
		       .andExpect(view().name("users/update-password-form"));
		
		verify(service, times(1)).saveUser(user);
	}

	@Test
	@WithMockUser(username = "user@gmail.com", authorities = "USER")
	public void updatePasswordInvalidFormTest() throws Exception {
		User user = new User(1L, "User", "user@gmail.com", "Password", Authority.USER);
		
		when(service.saveUser(user)).thenReturn(user);
		
		mockMvc.perform(
				post("/users/update")
				.param("id", "1")
				.param("name", "User")
				.param("email", "user@gmail.com")
				.param("password", "Password")
				.param("confirmPassword", "Password1")
				.param("authority", "USER")
				.param("enabled", "true")
				.with(csrf())
				)
		       .andExpect(status().isOk())
		       .andExpect(model().attributeHasFieldErrors("user", "confirmPassword"))
		       .andExpect(model().attribute("user", hasProperty("email", is("user@gmail.com"))))
		       .andExpect(view().name("users/update-password-form"));
		
		verify(service, times(0)).saveUser(user);
	}

}
