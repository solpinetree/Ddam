package com.ddam.spring.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.repository.CrewRepository;

@Service
public class CrewService {
	
	@Autowired
    private CrewRepository crewRepository;
	
	@Autowired
	private FollowRequestService followRequestService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private LikesService likesService;
	
	@Autowired
	private MeetupService meetupService;
	
	@Transactional
	public void deleteById(long id) {
		followRequestService.deleteByToCrewId(id);
		followService.deleteByToCrewId(id);
		likesService.deleteByToCrewId(id);
		meetupService.deleteByCrewId(id);
		crewRepository.deleteById(id);
	}
	
	public Crew findById(long id) {
		return crewRepository.findById(id);
	}
	
	public Crew findCrews(Long id) {
		  return crewRepository.findById(id).orElse(null);
	}
	
	public void save(Crew crew) {
		crewRepository.save(crew);
	}
	
}
