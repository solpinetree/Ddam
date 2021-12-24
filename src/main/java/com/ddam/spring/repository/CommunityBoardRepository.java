package com.ddam.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ddam.spring.domain.CommunityBoard;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard, Long>{

	// LIKE username
	Page<CommunityBoard> findByUsernameContains(String sKeyword, Pageable pageable);

	// LIKE subject
	Page<CommunityBoard> findBySubjectContains(String sKeyword, Pageable pageable);

	// LIKE content
	Page<CommunityBoard> findByContentContains(String sKeyword, Pageable pageable);

	// LIKE subject + content
	@Query(value = "SELECT * FROM community_board WHERE subject LIKE %:sKeyword% OR content LIKE %:sKeyword% ORDER BY cb_id DESC", nativeQuery = true)
	Page<CommunityBoard> findBySbjOrCntContainsOrderByIdDesc(@Param("sKeyword") String sKeyword, Pageable pageable);
	
}
