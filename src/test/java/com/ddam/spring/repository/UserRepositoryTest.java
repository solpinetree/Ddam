package com.ddam.spring.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ddam.spring.domain.User;

@SpringBootTest // 스프링 context 를 로딩하여 테스트에 사용
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	User user1 = null,user2 = null;
	List<Long> ids = null;
	
	@Test
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

}
