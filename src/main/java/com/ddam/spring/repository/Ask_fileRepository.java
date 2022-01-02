package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Ask_board;
import com.ddam.spring.domain.Ask_file;

public interface Ask_fileRepository extends JpaRepository<Ask_file, Long>{
	
	List<Ask_file> findByBoard(Ask_board board);
	
}
