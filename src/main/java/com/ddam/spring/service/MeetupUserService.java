package com.ddam.spring.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Meetup;
import com.ddam.spring.domain.MeetupUser;
import com.ddam.spring.repository.MeetupRepository;
import com.ddam.spring.repository.MeetupUserRepository;
import com.ddam.spring.repository.UserRepository;

@Service
public class MeetupUserService {

	@Autowired
	MeetupUserRepository meetupUserRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MeetupRepository meetupRepository;
	
	
	public int find(long uid, long mid) {
		if(meetupUserRepository.countByUserIdAndMeetupId(uid, mid) != 0) {	//이미 참가 신청한 경우
			return 1;
		}else {		// 참가 신청 안한 상태
			return 0;
		}
	}
	
	@Transactional
	public void save(long uid, long mid) {
		Meetup meetup = meetupRepository.findById(mid);
		MeetupUser meetupUser = new MeetupUser();
		meetupUser.setUser(userRepository.findById(uid));
		meetupUser.setMeetup(meetupRepository.findById(mid));
		meetupUserRepository.save(meetupUser);
		meetup.getParticipantList().add(meetupUserRepository.findByUserIdAndMeetupId(uid, mid));
	}
	
	@Transactional
	public void delete(long uid, long mid) { // 참가취소
		Meetup meetup = meetupRepository.findById(mid);
		meetup.getParticipantList().remove(meetupUserRepository.findByUserIdAndMeetupId(uid, mid));
		meetupUserRepository.deleteByUserIdAndMeetupId(uid, mid);
	}

	public void deleteByMeetup(Meetup m) {
		meetupUserRepository.deleteByMeetup(m);
	}
}
