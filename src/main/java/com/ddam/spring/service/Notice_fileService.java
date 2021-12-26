package com.ddam.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.Notice_board;
import com.ddam.spring.domain.Notice_file;
import com.ddam.spring.repository.Notice_boardRepository;
import com.ddam.spring.repository.Notice_fileRepository;

@Service
public class Notice_fileService {

	private Notice_fileRepository repository;
	
	@Autowired
	public void setRepository(Notice_fileRepository repository) {
		this.repository = repository;
	}

	private Notice_boardRepository boardrepository;

	@Autowired
	public void setBoardrepository(Notice_boardRepository boardrepository) {
		this.boardrepository = boardrepository;
	}

	public Notice_fileService() {
		System.out.println("Notice_fileService() 생성");
	}

	//공지사항 작성페이지(파일첨부)
	@Transactional
	public Notice_file saveNotice_file(Notice_file dto) {
		repository.save(dto);
		return dto;
	}
	
	//공지사항 뷰페이지(파일첨부)
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<Notice_file> viewByNfid(long nbid){
		
		Notice_board board = boardrepository.findById(nbid).orElse(null);
		
		List<Notice_file> files = repository.findByBoard(board);
		
		return files;  // 읽어오기
	}
	
	//공지사항 읽어오기(파일첨부)
	public List<Notice_file> selectByNfid(long nbid){
		
		Notice_board board = boardrepository.findById(nbid).orElse(null);
		List<Notice_file> files = repository.findByBoard(board);
		files = repository.findByBoard(board);
		Notice_file file = files.get(0);
		return files;
	}
	
	//공지사항 수정(파일첨부)
	public int update(Notice_file file) {
		int cnt = 0;
		Notice_file data = repository.findById(file.getNfid()).orElse(null);
		if(data != null) {
			data.setFilename(file.getFilename());
			data.setOriginalname(file.getOriginalname());
			repository.saveAndFlush(data);  // UPDATE
//			repository.save(file);
			cnt = 1;
		}
		return cnt;
	}
	
	//공지사항 삭제
		public int deleteByUid(long abid) {
			Notice_board board = boardrepository.findById(abid).orElse(null);
			List<Notice_file> files = repository.findByBoard(board);
			repository.deleteById(abid);
			repository.flush();		
			return 1;
		}

}
