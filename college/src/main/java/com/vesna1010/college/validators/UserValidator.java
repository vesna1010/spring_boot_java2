package com.vesna1010.college.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.vesna1010.college.models.User;
import com.vesna1010.college.services.UserService;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserService service;

	@Override
	public boolean supports(Class<?> cls) {
		return (cls == User.class);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user = (User) object;

		if (!user.getName().matches("^[a-zA-Z\\s]{3,}$")) {
			errors.rejectValue("name", "user.name");
		}

		if (!user.getEmail().matches("^[a-zA-Z0-9_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
			errors.rejectValue("email", "user.email.pattern");
		}

		if (service.existsUserByEmail(user.getEmail())) {
			errors.rejectValue("email", "user.email.duplicated");
		}

		if (user.getPassword().matches("^\\S{8,15}$")) {
			if (!user.getPassword().equals(user.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "user.passwordConfirmDifferent");
			}
		} else {
			errors.rejectValue("password", "user.password");
		}

		if (user.getAuthority() == null) {
			errors.rejectValue("authority", "user.authority");
		}

	}

}