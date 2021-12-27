package com.ddam.spring.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddam.spring.dto.UserFormDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Builder
@RequiredArgsConstructor
@Entity  // javax.persistence.Entity
@ToString
@Table(
		name = "user"   // db 테이블명
		, indexes = {@Index(columnList="id")}  // 컬럼에 대한 index 생성
		, uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}   // unique 제약사항
		)
// 회원 엔티티
public class User implements UserDetails{
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
	
	@Column(name = "auth")
    private String auth;
	
	@OneToMany(mappedBy="user",fetch = FetchType.EAGER)
	private Set<MeetupUser> participantList = new HashSet<>();

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
        return user;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

   
}































