package com.ddam.spring.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.FollowRequest;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long>{

	int countByFromUserIdAndToCrewId(long fromUserId, long toCrewId);
	
	int countByToCrewId(long toCrewId);
	
	List<FollowRequest> findByToCrewId(long toCrewId);
	
	@Transactional
	void deleteByFromUserIdAndToCrewId(long fromUserId, long toCrewId);

	void deleteByToCrewId(long toCrewId);

	List<FollowRequest> findAllByToCrewId(long cid);
	
}
