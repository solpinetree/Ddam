package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.User;
import com.ddam.spring.repository.UserRepository;

import lombok.AllArgsConstructor;

@EnableAutoConfiguration
@AllArgsConstructor
@Service("userDetailsService")  // 빈 등록하기
public class UserService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	
	
	@Transactional
	public User saveUser(User user){

        validateDuplicateMember(user);
//        String hashPw = bCryptPasswordEncoder.encode(user.getPassword());
//        user.setPassword(hashPw);

       return userRepository.save(user);
        
    }
	
	private void validateDuplicateMember(User user){
		User findUser = userRepository.findByUsername(user.getUsername());
        if(findUser != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

	public User loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        
        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new SimpleGrantedAuthority(user.getRole()));
//
//        UserContext userContext = new UserContext(user,roles);

        return User.builder()
        		.id(user.getId()) // 이영진!!!!!!!! by 강사.
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
	

}
