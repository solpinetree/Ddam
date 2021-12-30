package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.CrewChat;

public interface  CrewChatRepository extends JpaRepository<CrewChat, Long> {
	List<CrewChat> findByWriterId(long writerId);
}
