package com.ddam.spring.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Meetup;

public interface MeetupRepository extends JpaRepository<Meetup, Long>{

	Meetup save(Meetup meetup);

	List<Meetup> findByCrewId(long crewId);
	
	List<Meetup> findByDatetimeGreaterThanOrderByDatetimeAsc(LocalDateTime now);

	Meetup findById(long id);

	List<Meetup> findByCrewIdAndDatetimeGreaterThanOrderByDatetimeAsc(long cid, LocalDateTime minusDays);

	void deleteByCrewId(long crewId);

	List<Meetup> findAllByCrewId(long crewId);
}
