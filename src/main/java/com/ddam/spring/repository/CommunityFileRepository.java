package com.ddam.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.CommunityFile;

public interface CommunityFileRepository extends JpaRepository<CommunityFile, Long> {

}
