package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.CommunityLike;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

}
