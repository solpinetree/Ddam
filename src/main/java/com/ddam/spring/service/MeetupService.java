package com.ddam.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.domain.Meetup;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.MeetupRepository;

@Service
public class MeetupService {
	
	@Autowired
	MeetupRepository meetupRepository;
	
	@Autowired
	MeetupUserService meetupUserService;
	
	@Autowired
	CrewRepository crewRepository;
	
	public Meetup save(long cid, Meetup meetup) {
		
		Crew crew = crewRepository.findById(cid);
		Meetup res = meetupRepository.save(meetup);
//		crew.getMeetupLists().add(res);
		return res;
	}
	
	@Transactional
	public void deleteByCrewId(long crewId) {
		meetupRepository.deleteByCrewId(crewId);
	}
	
	public void deleteById(long meetupId) {
		meetupRepository.deleteById(meetupId);
	}
}
