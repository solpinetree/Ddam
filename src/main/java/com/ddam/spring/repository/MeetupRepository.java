package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Meetup;

public interface MeetupRepository extends JpaRepository<Meetup, Long>{

	Meetup save(Meetup meetup);

	List<Meetup> findByCrewId(long crewId);

}
