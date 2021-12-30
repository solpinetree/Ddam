package com.ddam.spring.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddam.spring.domain.User;

@SpringBootTest // 스프링 context 를 로딩하여 테스트에 사용
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	User user1 = null,user2 = null;
	List<Long> ids = null;
	
//	@Test
	void crud() { // create - read - update - delete
		System.out.println("=====#TEST#================================================");
		
		User user1 = null,user2 = null;
		List<Long> ids = null;
		
		
		userRepository.saveAndFlush(new User()); // insert,저장하기
		userRepository.findAll().forEach(System.out::println);
		
		Long count = userRepository.count();
        System.out.println(count);

        
//        userRepository.deleteByUsername("크루원1"); //delete

		
		System.out.println("=====================================================");
		
	}
	
	@Test
	void addAdmin() {
		User user = User.builder()
				.username("admin")
				.name("관리자")
				.gender("F")
				.email("admin@mail.com")
				.phone("000-111-2222")
				.password(passwordEncoder.encode("1234"))
				.role("ROLE_ADMIN")
				.build();
		
		userRepository.saveAndFlush(user);
		
		user = User.builder()
				.username("user")
				.name("유저")
				.gender("F")
				.email("admin@mail.com")
				.phone("000-111-2222")
				.password(passwordEncoder.encode("1111"))
				.role("ROLE_USER")
				.build();
		
		userRepository.saveAndFlush(user);
	}

}
