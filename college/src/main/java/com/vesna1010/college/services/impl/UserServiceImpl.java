package com.vesna1010.college.services.impl;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vesna1010.college.models.User;
import com.vesna1010.college.repositories.UserRepository;
import com.vesna1010.college.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Page<User> findAllUsers(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public User findUserById(Long id) {
		Optional<User> optional = repository.findById(id);

		return optional.orElseThrow(() -> new RuntimeException("No user found with id " + id));
	}

	@Override
	public User findUserByEmail(String email) {
		Optional<User> optional = repository.findByEmail(email);

		return optional.orElseThrow(() -> new RuntimeException("No user found with email " + email));
	}

	@Override
	public User saveUser(User user) {
		user = setAndGetUserWithEncodedPassword(user);

		return repository.save(user);
	}

	private User setAndGetUserWithEncodedPassword(User user) {
		String password = user.getPassword();
		String encodedPassword = encoder.encode(password);
		
		user.setPassword(encodedPassword);

		return user;
	}

	@Override
	public void deleteUserById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public void disableUserById(Long id) {
		repository.disableById(id);
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return repository.existsByEmail(email);
	}

}
