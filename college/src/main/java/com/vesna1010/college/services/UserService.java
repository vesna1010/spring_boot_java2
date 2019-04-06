package com.vesna1010.college.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.vesna1010.college.models.User;

public interface UserService {

	Page<User> findAllUsers(Pageable pageable);

	User findUserById(Long id);

	User findUserByEmail(String email);

	User saveUser(User user);

	boolean existsUserByEmail(String email);

	void deleteUserById(Long id);

	void disableUserById(Long id);

}
