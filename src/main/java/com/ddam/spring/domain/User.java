package com.ddam.spring.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.constant.Role;
import com.ddam.spring.domain.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity  // javax.persistence.Entity
@ToString
@Table(
		name = "user"   // db 테이블명
		, indexes = {@Index(columnList="id")}  // 컬럼에 대한 index 생성
		, uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}   // unique 제약사항
		)
// 회원 엔티티
public class User {
	@Id   // PK.  
	@GeneratedValue  // AI
	private Long id;
	
	@NonNull
	private String username;
	
	@NonNull
	private String password;
	
	@NonNull
    private String name;
	
	@NonNull
	private String gender;
	
	@NonNull
    private String email;
	
	@NonNull
    private String phone;
	
	@Enumerated(EnumType.STRING)
    private Role auth;

	public static User createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder){
		System.out.println(userFormDto);
		User user = new User();
		user.setUsername(userFormDto.getUsername());
		user.setName(userFormDto.getName());
		user.setGender(userFormDto.getGender());
		user.setEmail(userFormDto.getEmail());
        user.setPhone(userFormDto.getPhone());
//        String password = passwordEncoder.encode(userFormDto.getPassword());
        user.setPassword(userFormDto.getPassword());
        user.setAuth(Role.USER);
        return user;
    }

	

   
}































