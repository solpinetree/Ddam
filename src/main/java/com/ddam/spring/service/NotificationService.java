package com.ddam.spring.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.domain.Notification;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	NotificationRepository notificationRepository;

	public void save(Notification notification) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		notification.setWritetime(timestamp);
		
		notificationRepository.save(notification);
	}
	

	public List<Notification> findAllByUserId(long uid){
		return notificationRepository.findAllByUserId(uid);
	}
	

}
