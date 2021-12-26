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

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserFormDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
    	UserFormDto userFormDto = (UserFormDto) obj;
    	
        if(userRepository.findByUsername(((UserFormDto) obj).getUsername()) !=null){
            // 이름이 존재하면
            errors.rejectValue("username", "key","이미 사용자 이름이 존재합니다.");
        }

    }// 비밀번호 검사할때 쓰면 될듯
}
