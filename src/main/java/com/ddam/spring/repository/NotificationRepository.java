package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByUserId(long uid);
	
	Notification findById(long id);
	
	Notification save(Notification notification);

}
