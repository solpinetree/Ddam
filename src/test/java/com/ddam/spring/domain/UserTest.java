package com.ddam.spring.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void test() {
		User user1 = new User();
		user1.setUsername("크루원1");
		user1.setName("이영진");
		user1.setPassword("1111");
		user1.setGender("남");
		user1.setEmail("dudwls@gmail.com");
		user1.setPhone("010-5521-8453");
		
		User user2 = User.builder()
				.username("크루원2")
				.name("이진영")
				.password("1111")
				.gender("여")
				.email("asdfas@gmail.com")
				.phone("111-1111-111")
				.build()
				;
		
		System.out.println(user1);
		System.out.println(user2);
	}

}
