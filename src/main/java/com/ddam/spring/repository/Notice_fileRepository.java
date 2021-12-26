package com.ddam.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddam.spring.domain.Notice_board;
import com.ddam.spring.domain.Notice_file;

public interface Notice_fileRepository extends JpaRepository<Notice_file, Long>{
	
	List<Notice_file> findByBoard(Notice_board board);
}
