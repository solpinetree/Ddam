package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Meetup;
import com.ddam.spring.domain.MeetupUser;

public interface MeetupUserRepository extends JpaRepository<MeetupUser, Long>{

	int countByUserIdAndMeetupId(long uid, long mid);

	void deleteByUserIdAndMeetupId(long uid, long mid);
	
	MeetupUser findByUserIdAndMeetupId(long uid, long mid);

	void deleteByMeetup(Meetup m);
}
