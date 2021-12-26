package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.Notice_board;
import com.ddam.spring.domain.Notice_file;
import com.ddam.spring.repository.Notice_boardRepository;
import com.ddam.spring.repository.Notice_fileRepository;

@Service
public class Notice_boardService {

	@Autowired
	private Notice_boardRepository repository;
	
	@Autowired
	private Notice_fileRepository filerepository;

	public Notice_boardService() {
		System.out.println("Notice_boardService() 생성");
	}
	
	// 공지사항 게시판 목록페이지
	public List<Notice_board> list() {
		System.out.println("Notice_board list 메서드 진입");
		return repository.findAll(Sort.by(Direction.ASC, "nbid"));
	}

	// 공지사항 작성페이지(제목, 내용)
	@Transactional
	public Long saveNotice_board(Notice_board dto) {
		repository.save(dto);
		return (long) 1;
	}
	
	// 공지사항 뷰페이지(제목, 내용)
	@Transactional
	public List<Notice_board> viewByNbid(long nbid){
		
		List<Notice_board> list = new ArrayList<>();
		
		Notice_board data = repository.findById(nbid).orElse(null); // SELECT
		if(data != null) {
			data.setView_cnt(data.getView_cnt() + 1);
			repository.saveAndFlush(data);  // UPDATE
			list.add(data);
		}
		return list;  // 읽어오기
	}
	
	// 공지사항 읽어오기(제목, 내용)
	public List<Notice_board> selectByNbid(long nbid){
		List<Notice_board> list = new ArrayList<>();
		list.add(repository.findById(nbid).orElse(null));
		return list;
	}
	
	// 공지사항 수정(제목, 내용)
	public int update(Notice_board dto, String originalFileName) {
		int cnt = 0;
		Notice_board data = repository.findById(dto.getNbid()).orElse(null);
		List<Notice_file> files = filerepository.findByBoard(data);
		Notice_file file = files.get(0); 
		
		if(data != null) {
				
			data.setContent(dto.getContent());
			data.setSubject(dto.getSubject());
			file.setOriginalname(originalFileName);
			file.setFilename(originalFileName);
			
			repository.saveAndFlush(data);  // UPDATE
			filerepository.saveAndFlush(file);
			cnt = 1;
		}
		return cnt;
	}
	
	// 공지사항 삭제
	public int deleteByUid(long nbid) {
		Notice_board board = repository.findById(nbid).orElse(null);
		List<Notice_file> files = filerepository.findByBoard(board);
		files.forEach(i -> filerepository.delete(i));
		repository.deleteById(nbid);
		repository.flush();
		return 1;
	}


}
