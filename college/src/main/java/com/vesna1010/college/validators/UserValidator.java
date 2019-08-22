package com.vesna1010.college.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.vesna1010.college.models.User;
import com.vesna1010.college.services.UserService;

@Component
public class UserValidator implements Validator {

	public static final String NAME_REGEX = "^[a-zA-Z\\s]{3,}$";
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
	public static final String PASSWORD_REGEX = "^\\S{8,15}$";

	@Autowired
	private UserService service;

	@Override
	public boolean supports(Class<?> cls) {
		return (cls == User.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user = (User) object;

		if (isInvalidName(user.getName())) {
			errors.rejectValue("name", "user.name");
		}

		if (isInvalidEmail(user.getEmail())) {
			errors.rejectValue("email", "user.email.pattern");
		}

		if (service.existsUserByEmail(user.getEmail())) {
			errors.rejectValue("email", "user.email.duplicated");
		}

		if (isInvalidPassword(user.getPassword())) {
			errors.rejectValue("password", "user.password");
		} else {
			if (isDifferentPasswords(user.getPassword(), user.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "user.passwordConfirmDifferent");
			}
		}

		if (user.getAuthority() == null) {
			errors.rejectValue("authority", "user.authority");
		}

	}

	private boolean isInvalidName(String name) {
		return (name == null || !name.matches(NAME_REGEX));
	}

	private boolean isInvalidEmail(String email) {
		return (email == null || !email.matches(EMAIL_REGEX));
	}

	private boolean isInvalidPassword(String password) {
		return (password == null || !password.matches(PASSWORD_REGEX));
	}

	public boolean isDifferentPasswords(String password, String confirmPassword) {
		return !password.equals(confirmPassword);
	}

}
