package com.ddam.spring.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ddam.spring.domain.User;

import lombok.Builder;
import lombok.Data;


 @Data
public class UserFormDto {
	// 회원가입 화면으로부터 넘어오는 가입정보를 담을 dto
	
	@NotBlank(message= "닉네임은 필수 입력 값입니다.")
	private String username;

	@NotBlank(message= "비밀번호는 필수 입력 값입니다.")
	private String password;
	
	@NotBlank(message= "이름은 필수 입력 값입니다.")
	private String name;
	
	@NotBlank(message= "성별은 필수 입력 값입니다.")
	private String gender;
	
	@NotBlank(message= "이메일은 필수 입력 값입니다.")
	private String email;
	
	@NotBlank(message= "전화번호는 필수 입력 값입니다.")
	private String phone;
	
	public User toEntity(){
        User build = User.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password))
                .name(name)
                .gender(gender)
                .email(email)
                .phone(phone)
                .build();
        return build;
    }

	
}
