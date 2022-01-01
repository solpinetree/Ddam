package com.ddam.spring.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ddam.spring.domain.Follow;
import com.ddam.spring.domain.User;

public interface FollowRepository extends JpaRepository<Follow, Long>{

	@Modifying
	@Transactional
	void deleteByFromUserIdAndToCrewId(long fromUserId, long toCrewId);	// 언팔로우 메서드
	
	int countByToCrewId(long cid);	// 크루 멤버 몇명인지 count

	int countByFromUserIdAndToCrewId(long fromUserId, long toCrewId);	 // 팔로우 되어있는지 count 하는 메서드

	List<Follow> findAllByToCrewId(long toCrewId);
	
	List<Follow> findByFromUserId(Long fromUserId);

	void deleteByToCrewId(long toCrewId);


}
