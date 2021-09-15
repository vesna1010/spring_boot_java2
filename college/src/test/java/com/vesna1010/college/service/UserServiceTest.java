package com.vesna1010.college.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	private UserRepository repository;
	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	public void findAllUsersTest() {
		when(repository.findAll(PAGEABLE))
				.thenReturn(new PageImpl<User>(Arrays.asList(new User(1L, "User A"), new User(2L, "User B"))));

		Page<User> page = service.findAllUsers(PAGEABLE);
		List<User> users = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(users, hasSize(2));
		assertThat(users.get(0).getName(), is("User A"));
		assertThat(users.get(1).getName(), is("User B"));
		verify(repository, times(1)).findAll(PAGEABLE);
	}

	@Test
	public void findUserByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(new User(1L, "User")));

		User user = service.findUserById(1L);

		assertThat(user.getName(), is("User"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void findUserByIdNotFoundTest() {
		when(repository.findById(5L)).thenReturn(Optional.empty());

		service.findUserById(5L);
	}

	@Test
	public void saveUserTest() {
		User user = new User(1L, "User", "user@gmail.com", "Passowrd", Authority.ADMIN);

		when(repository.save(user)).thenReturn(user);
		when(passwordEncoder.encode("Passowrd")).thenReturn("EncodedPassword");

		User userSaved = service.saveUser(user);

		assertThat(userSaved.getId(), is(1L));
		assertThat(userSaved.getPassword(), is("EncodedPassword"));
		verify(passwordEncoder, times(1)).encode("Passowrd");
		verify(repository, times(1)).save(user);
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

	@Test
	public void existsUserByIdTest() {
		when(repository.existsByEmail("userA@gmail.com")).thenReturn(true);

		boolean isExist = service.existsUserByEmail("userA@gmail.com");

		assertTrue(isExist);
		verify(repository, times(1)).existsByEmail("userA@gmail.com");
	}

}
