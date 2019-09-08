package com.vesna1010.college.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vesna1010.college.enums.Authority;
import com.vesna1010.college.models.User;
import com.vesna1010.college.repositories.UserRepository;

@Transactional
public class UserRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder encoder;

	@Test
	public void findAllUsersWithPageableTest() {
		Page<User> page = repository.findAll(PAGEABLE);
		List<User> users = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(users, hasSize(3));
		assertThat(users.get(0).getName(), is("User A"));
		assertThat(users.get(1).getName(), is("User B"));
		assertThat(users.get(2).getName(), is("User C"));
	}

	@Test
	public void findUserByIdTest() {
		Optional<User> optional = repository.findById(1L);
		User user = optional.get();

		assertThat(user.getName(), is("User A"));
		assertThat(user.getEmail(), is("userA@gmail.com"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
	}

	@Test
	public void findUserByIdNotFoundTest() {
		Optional<User> optional = repository.findById(4L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void findUserByEmailTest() {
		Optional<User> optional = repository.findByEmail("userA@gmail.com");
		User user = optional.get();

		assertThat(user.getId(), is(1L));
		assertThat(user.getName(), is("User A"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
	}

	@Test
	public void findUserByEmailNotFoundTest() {
		Optional<User> optional = repository.findByEmail("userEgmail.com");

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveUserTest() {
		User user = new User("User E", "userEgmail.com", encoder.encode("PasswordE"), Authority.USER);

		user = repository.save(user);

		Long id = user.getId();

		assertTrue(repository.existsById(id));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateUserTest() {
		Optional<User> optional = repository.findByEmail("userA@gmail.com");
		User user = optional.get();
		String password = encoder.encode("NewPassword");

		user.setPassword(password);
		user = repository.save(user);

		assertThat(user.getPassword(), is(password));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void existsUserByEmailTest() {
		assertTrue(repository.existsByEmail("userA@gmail.com"));
		assertFalse(repository.existsByEmail("userE@gmail.com"));
	}

	@Test
	public void deleteUserByIdTest() {
		repository.deleteById(3L);

		assertFalse(repository.existsById(3L));
		assertThat(repository.count(), is(2L));
	}

	@Test
	public void disableUserByIdTest() {
		repository.disableById(3L);

		Optional<User> optional = repository.findById(3L);
		User user = optional.get();

		assertFalse(user.isEnabled());
	}

}
