package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.CommunityLike;
import com.ddam.spring.domain.User;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

	CommunityLike findByUserAndCommunityBoard(User user, CommunityBoard communityBoard);

}
