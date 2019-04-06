package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vesna1010.college.enums.Authority;
import com.vesna1010.college.models.User;
import com.vesna1010.college.repositories.UserRepository;
import com.vesna1010.college.services.UserService;

public class UserServiceTest extends BaseServiceTest {

	@Autowired
	private UserService service;
	@MockBean
	private PasswordEncoder encoder;
	@MockBean
	private UserRepository repository;

	@Test
	public void findAllUsersTest() {
		when(repository.findAll(PAGEABLE)).thenReturn(new PageImpl<User>(Arrays.asList(user1, user2, user3)));

		Page<User> page = service.findAllUsers(PAGEABLE);
		List<User> users = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(users, hasSize(3));
		assertThat(users.get(0).getName(), is("User A"));
		assertThat(users.get(1).getName(), is("User B"));
		assertThat(users.get(2).getName(), is("User C"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findUserByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(user1));

		User user = service.findUserById(1L);

		assertThat(user.getName(), is("User A"));
		assertThat(user.getEmail(), is("userA@gmail.com"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findUserByIdNotFoundTest() {
		when(repository.findById(4L)).thenReturn(Optional.empty());

		service.findUserById(4L);
	}

	@Test
	public void findUserByEmailTest() {
		when(repository.findByEmail("userA@gmail.com")).thenReturn(Optional.of(user1));

		User user = service.findUserByEmail("userA@gmail.com");

		assertThat(user.getId(), is(1L));
		assertThat(user.getName(), is("User A"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
		verify(repository, times(1)).findByEmail("userA@gmail.com");
	}

	@Test(expected = RuntimeException.class)
	public void findUserByEmailNotFoundTest() {
		when(repository.findByEmail("userA@gmail.com")).thenReturn(Optional.empty());

		service.findUserById(1L);
	}

	@Test
	public void saveUserTest() {
		User user = new User("User", "user@gmail.com", "Password", Authority.USER);

		when(encoder.encode("Password")).thenReturn("Encoded Password");
		when(repository.save(user))
				.thenReturn(new User(4L, "User", "user@gmail.com", "Encoded Password", Authority.USER));

		User userSaved = service.saveUser(user);

		assertThat(userSaved.getId(), is(4L));
		verify(repository, times(1)).save(user);
		verify(encoder, times(1)).encode("Password");
	}

	@Test
	public void existsUserByEmailTest() {
		when(repository.existsByEmail("userA@gmail.com")).thenReturn(true);

		boolean isExistsUser = service.existsUserByEmail("userA@gmail.com");

		assertTrue(isExistsUser);
		verify(repository, times(1)).existsByEmail("userA@gmail.com");
	}

	@Test
	public void deleteUserByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteUserById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

	@Test
	public void disableUserByIdTest() {
		doNothing().when(repository).disableById(1L);

		service.disableUserById(1L);

		verify(repository, times(1)).disableById(1L);
	}

}
