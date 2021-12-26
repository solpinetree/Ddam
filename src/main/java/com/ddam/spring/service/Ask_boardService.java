package com.ddam.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.Ask_board;
import com.ddam.spring.domain.Ask_file;
import com.ddam.spring.repository.Ask_boardRepository;
import com.ddam.spring.repository.Ask_fileRepository;

@Service
public class Ask_boardService {

	@Autowired
	private Ask_boardRepository repository;
	
	@Autowired
	private Ask_fileRepository filerepository;

	public Ask_boardService() {
		System.out.println("Ask_boardService() 생성");
	}
	
	// 문의사항 게시판 목록페이지
	public List<Ask_board> list() {
		// return repository.findBySubjectAndRegDate("subject", "regdate");
		System.out.println("Ask_board list 메서드 진입");
//		List<Ask_board> temp = repository.findAll(Sort.by(Direction.ASC, "abid"));
//		for(int i = 0; i < temp.size(); i++) {
//			System.out.println(temp.get(i).toString());
//		}
		return repository.findAll(Sort.by(Direction.ASC, "abid"));
	}

	// 문의사항 작성페이지(제목, 내용)
	@Transactional
	public Long saveAsk_board(Ask_board dto) {
//		dto.setRegdate(LocalDateTime.now());
		repository.save(dto);
		return (long) 1;
	}
	
	// 문의사항 뷰페이지(제목, 내용)
	// 특정 abid 의 글 읽어오기 + 조회수 증가
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<Ask_board> viewByAbid(long abid){
		
		List<Ask_board> list = new ArrayList<>();
		
		Ask_board data = repository.findById(abid).orElse(null); // SELECT
		if(data != null) {
			data.setView_cnt(data.getView_cnt() + 1);
			repository.saveAndFlush(data);  // UPDATE
			list.add(data);
		}
		return list;  // 읽어오기
	}
	
	//문의사항 읽어오기(제목, 내용)
	public List<Ask_board> selectByAbid(long abid){
		List<Ask_board> list = new ArrayList<>();
		list.add(repository.findById(abid).orElse(null));
		return list;
	}
	
	//문의사항 수정(제목, 내용)
	public int update(Ask_board dto, String originalFileName) {
		int cnt = 0;
		Ask_board data = repository.findById(dto.getAbid()).orElse(null);
		List<Ask_file> files = filerepository.findByBoard(data);
		Ask_file file = files.get(0); 
		
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
	
	//문의사항 삭제
	public int deleteByUid(long abid) {
		Ask_board board = repository.findById(abid).orElse(null);
		List<Ask_file> files = filerepository.findByBoard(board);
		files.forEach(i -> filerepository.delete(i));
		repository.deleteById(abid);
		repository.flush();
		return 1;
	}
	
	//문의사항 답변페이지(제목,내용)
	@Transactional
	public List<Ask_board> selectByAbid_Admin(long abid){
		List<Ask_board> list = new ArrayList<>();
		list.add(repository.findById(abid).orElse(null));
		return list;
	}	

	// 문의사항 답변페이지(답변작성)
	@Transactional
	public int update_admin(Ask_board dto) {
//		repository.save(dto);
//		return (long) 1;
		int cnt = 0;
		Ask_board data = repository.findById(dto.getAbid()).orElse(null);
		
		if(data != null) {
			data.setAnswer(dto.getAnswer());
			repository.saveAndFlush(data);  // UPDATE
			cnt = 1;
		}
		return cnt;
	}
	


}
