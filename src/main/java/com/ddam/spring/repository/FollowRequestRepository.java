package com.ddam.spring.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.FollowRequest;
import com.ddam.spring.domain.User;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long>{

	int countByFromUserIdAndToCrewId(long fromUserId, long toCrewId);
	
	List<FollowRequest> findByToCrewId(long toCrewId);
	
	void deleteByFromUserIdAndToCrewId(long fromUserId, long toCrewId);
	
}
