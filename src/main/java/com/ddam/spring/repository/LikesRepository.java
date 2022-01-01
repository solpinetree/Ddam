package com.ddam.spring.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.ddam.spring.domain.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long>{

	@Modifying
	@Transactional
	void deleteByFromUserIdAndToCrewId(long fromUserId, long toCrewId);	// 좋아요 취소 메서드
	
	int countByToCrewId(long cid);	// 총 좋아요 count

	int countByFromUserIdAndToCrewId(long fromUserId, long toCrewId);	 // 좋아요 되어있는지 count 하는 메서드

	void deleteByToCrewId(long toCrewId);

}