package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.CommunityComment;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

}
