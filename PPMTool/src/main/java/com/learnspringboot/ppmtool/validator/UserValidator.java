package com.learnspringboot.ppmtool.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.learnspringboot.ppmtool.domain.User;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors) {
		User user = (User) object;
		if(user.getPassword().length() < 6) {
			errors.rejectValue("password", "length", "Password must be at least 6 characters long");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "match", "Passwords must match");
		}
	}

}
