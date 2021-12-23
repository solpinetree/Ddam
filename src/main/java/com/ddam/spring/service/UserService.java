package com.ddam.spring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.User;
import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.repository.UserRepository;


@Service
public class UserService {
	
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User saveUser(User user){
        validateDuplicateMember(user);
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

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

	

}
