package com.ddam.spring.controller;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddam.spring.domain.Notification;
import com.ddam.spring.repository.NotificationRepository;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.NotificationService;

@Controller
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/notification/{uid}")
	@ResponseBody
	private void chatlist(@PathVariable int uid) throws Exception {

		Notification ccc = new Notification();
		Collections.sort(userRepository.findById(uid).getNotifications(), ccc); // 역순으로 정렬하기
	}
	
	@PostMapping("/deleteNoti")
	@ResponseBody
	private void deleteNoti(@RequestParam HashMap<String, Object> param) throws Exception {

		long notiId = Long.parseLong((String)param.get("notiId"));
		long userId = Long.parseLong((String)param.get("userId"));
		
		Notification notification = notificationRepository.findById(notiId);
		userRepository.findById(userId).getNotifications().remove(notification);
		notificationRepository.deleteById(notiId);
	}
}
