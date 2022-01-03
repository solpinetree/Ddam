package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.FollowRequest;
import com.ddam.spring.domain.Notification;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.FollowRequestRepository;
import com.ddam.spring.repository.UserRepository;

@Service
public class FollowRequestService {
	@Autowired
	FollowRequestRepository followRequestRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CrewRepository crewRepository;
	@Autowired
	NotificationService notificationService;

	@Transactional
	public int requestSave(FollowRequest followRequest) { // 요청 저장하기
		
		Notification notification = new Notification();
		notification.setUser(followRequest.getToCrew().getCrewAdmin());
		notification.setNoti(followRequest.getToCrew().getName() + " 에 팔로우 요청이 왔습니다.");
		notificationService.save(notification);
		
		followRequestRepository.save(followRequest);
		
		
		return 1;
	}

	public boolean check(long fromUserId, long toCrewId) { 
		// 요청 중이면 true반환
		if(followRequestRepository.countByFromUserIdAndToCrewId(fromUserId, toCrewId) != 0)
			return false;
		else
			return true;
	}
	
	public List<User> requests(long toCrewId){
		List<FollowRequest> rList  = followRequestRepository.findByToCrewId(toCrewId);
		List<User> res = new ArrayList<>();
		
		for(FollowRequest fr : rList) {
			res.add(fr.getFromUser());
		}
		
		return res;
	}
	
	public void deleteByToCrewId(long toCrewId) {
		followRequestRepository.deleteByToCrewId(toCrewId);
	}
}
