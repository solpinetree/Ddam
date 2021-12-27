package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.MeetupUser;

public interface MeetupUserRepository extends JpaRepository<MeetupUser, Long>{

}
