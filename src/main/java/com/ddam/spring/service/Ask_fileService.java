package com.ddam.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.Ask_board;
import com.ddam.spring.domain.Ask_file;
import com.ddam.spring.repository.Ask_boardRepository;
import com.ddam.spring.repository.Ask_fileRepository;

@Service
public class Ask_fileService {

	private Ask_fileRepository repository;
	
	@Autowired
	public void setRepository(Ask_fileRepository repository) {
		this.repository = repository;
	}

	private Ask_boardRepository boardrepository;

	@Autowired
	public void setBoardrepository(Ask_boardRepository boardrepository) {
		this.boardrepository = boardrepository;
	}

	public Ask_fileService() {
		System.out.println("Ask_fileService() 생성");
	}

	//문의사항 작성페이지(파일첨부)
	@Transactional
	public Ask_file saveAsk_file(Ask_file dto) {
		repository.save(dto);
		return dto;
	}
	
	// 문의사항 뷰페이지(파일첨부)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<Ask_file> viewByAfid(long abid){
		
		Ask_board board = boardrepository.findById(abid).orElse(null);
		
		List<Ask_file> files = repository.findByBoard(board);
		
		return files;  // 읽어오기
	}
	
	//문의사항 읽어오기(파일첨부)
	public List<Ask_file> selectByAfid(long abid){
		
		Ask_board board = boardrepository.findById(abid).orElse(null);
		List<Ask_file> files = repository.findByBoard(board);
		return files;
	}
	
	//문의사항 수정(파일첨부)
	public int update(Ask_file file) {
		int cnt = 0;
		Ask_file data = repository.findById(file.getAfid()).orElse(null);
		if(data != null) {
			data.setFilename(file.getFilename());
			data.setOriginalname(file.getOriginalname());
			repository.saveAndFlush(data);  // UPDATE
			cnt = 1;
		}
		return cnt;
	}
	
	//문의사항 삭제
		public int deleteByUid(long abid) {
			Ask_board board = boardrepository.findById(abid).orElse(null);
			List<Ask_file> files = repository.findByBoard(board);
			repository.deleteById(abid);
			repository.flush();		
			return 1;
		}
		
		//문의사항 답변페이지(파일)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<Ask_file> selectByAfid_Admin(long abid){
		
		Ask_board board = boardrepository.findById(abid).orElse(null);
		List<Ask_file> files = repository.findByBoard(board);
		files = repository.findByBoard(board);
		Ask_file file = files.get(0);
		return files;
		
	}

}
