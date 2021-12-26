package com.ddam.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ddam.spring.domain.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

//	User deleteByUsername(String username);
}
