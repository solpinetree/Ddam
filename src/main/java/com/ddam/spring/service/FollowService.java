package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.domain.Follow;
import com.ddam.spring.domain.Notification;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.FollowRepository;
import com.ddam.spring.repository.FollowRequestRepository;
import com.ddam.spring.repository.NotificationRepository;
import com.ddam.spring.repository.UserRepository;

@Service
public class FollowService {
	
	@Autowired
	FollowRepository followRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CrewRepository crewRepository;
	@Autowired
	FollowRequestService followRequestService;
	@Autowired
	FollowRequestRepository followRequestRepository;
	
	@Autowired
	NotificationService notificationService;
	
	@Transactional
	public void save(long fromUserId, long toCrewId) { // 최종적으로 멤버가 됨
		Follow f = new Follow();
		
		User user = userRepository.findById(fromUserId);
		Crew crew = crewRepository.findById(toCrewId);
		
		f.setFromUser(user);
		f.setToCrew(crew);
		
		crew.setMemberCount(crew.getMemberCount()+1);
		
		followRepository.save(f);
		followRequestRepository.deleteByFromUserIdAndToCrewId(fromUserId, toCrewId);
		
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setNoti(crew.getName()+" 크루 멤버가 되었습니다.");
		notificationService.save(notification);	
		
	}
	
	@Transactional
	public void deleteByFromUserIdAndToCrewId(long fromUserId, long toCrewId) { // 크루 팔로우 거절당함
		followRepository.deleteByFromUserIdAndToCrewId(fromUserId, toCrewId);
		Crew crew = crewRepository.findById(toCrewId);
		User user = userRepository.findById(fromUserId);
		crew.setMemberCount(crew.getMemberCount()-1);
	}

	public String find(long fromUserId, long toCrewId) { // 팔로우가 되어있는지를 확인하기위해
		if(crewRepository.findById(toCrewId).getCrewAdmin().getId()==fromUserId) { // 지금 로그인한 유저가 주인장일경우
			return "admin";
		}else if(followRepository.countByFromUserIdAndToCrewId(fromUserId, toCrewId) == 0) {	// 팔로우 안되어있음
			
			if(followRequestService.check(fromUserId, toCrewId)==false) {	
				return "requested";
			}else {
				return "user";
			}
			
		}else {		// 팔로우 되어 있음
			return "member";
		}
	}
	
	public List<User> findMembers(long toCrewId){
		List<Follow> follows = followRepository.findAllByToCrewId(toCrewId);
		List<User> members = new ArrayList<>();
		for(Follow f : follows) {
			members.add(f.getFromUser());
		}
		
		return members;
	}
	
	public void deleteByToCrewId(long toCrewId) {
		followRepository.deleteByToCrewId(toCrewId);
	}
	
}
