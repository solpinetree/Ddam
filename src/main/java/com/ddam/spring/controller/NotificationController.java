package com.ddam.spring.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@PostMapping("/notification/list/{uid}")
	@ResponseBody
	private List<Notification> notilist(@PathVariable long uid) throws Exception {
		
		List<Notification> res = notificationRepository.findAllByUserId(uid);
		Notification noti = new Notification();
		Collections.sort(res, noti);
		return res;
		
	}
	
	@PostMapping("/deleteNoti")
	@ResponseBody
	private void deleteNoti(@RequestParam HashMap<String, Object> param) throws Exception {

		long notiId = Long.parseLong((String)param.get("notiId"));
		
		notificationRepository.deleteById(notiId);
	}
	
	@PostMapping("/notification/count")
	public @ResponseBody Map<String, Integer> count(Model model,@RequestParam Map<String, Object> paramMap) {
		
		long userId = Long.parseLong((String)paramMap.get("userId"));
		
		Map<String, Integer> res = new HashMap<>();
		
		// 해당 유저의 알림 수를 구함
		int count = notificationRepository.countByUserId(userId);
		
		res.put("count", count);
		
		return res;
	}
}
