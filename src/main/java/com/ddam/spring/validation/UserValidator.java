package com.ddam.spring.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.repository.UserRepository;

@Component
public class UserValidator implements Validator {

	
	

	@Override
	public boolean supports(Class<?> clazz) {
		return UserFormDto.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UserFormDto userFormDto = (UserFormDto) obj;


		// 닉네임이 비어있을 때
		String username = userFormDto.getUsername();
		if (username == null || username.trim().isEmpty()) {
			errors.rejectValue("username", "emptyUsername");
		}
		// 이름이 비어있을 때
		String name = userFormDto.getName();
		if (name == null || name.trim().isEmpty()) {
			errors.rejectValue("name", "emptyName");
		}
		// 비밀번호가 비어있을 때
		String password = userFormDto.getPassword();
		if (password == null || password.trim().isEmpty()) {
			errors.rejectValue("password", "emptyPassword");
		}	
		// 이메일이 비어있을 때
		String email = userFormDto.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.rejectValue("email", "emptyEmail");
		}

	}
}
