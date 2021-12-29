package com.ddam.spring.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.CommunityComment;
import com.ddam.spring.domain.CommunityFile;
import com.ddam.spring.domain.CommunityLike;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.CommunityBoardRepository;
import com.ddam.spring.repository.CommunityCommentRepository;
import com.ddam.spring.repository.CommunityFileRepository;
import com.ddam.spring.repository.CommunityLikeRepository;
import com.ddam.spring.repository.UserRepository;

@Service
public class CommunityBoardService {

	private CommunityBoardRepository communityBoardRepository;

	@Autowired
	public void setRepository(CommunityBoardRepository communityBoardRepository) {
		this.communityBoardRepository = communityBoardRepository;
	}
	
	private CommunityLikeRepository communityLikeRepository;
	
	@Autowired
	public void setCommunityLikeRepository(CommunityLikeRepository communityLikeRepository) {
		this.communityLikeRepository = communityLikeRepository;
	}
	
	private CommunityCommentRepository communityCommentRepository;
	
	@Autowired
	public void setCommunityCommentRepository(CommunityCommentRepository communityCommentRepository) {
		this.communityCommentRepository = communityCommentRepository;
	}
	
	private CommunityFileRepository communityFileRepository;
	
	@Autowired
	public void setCommunityFileRepository (CommunityFileRepository communityFileRepository) {
		this.communityFileRepository = communityFileRepository;
	}
	
	private UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public CommunityBoardService() {
		System.out.println("CommunityBoardService() 생성");
	}
	
	
	// 유저 정보 가져오기
	public User loadUser(String username) {
		User user = userRepository.findByUsername(username);
		
		return user;
	}

	// 목록
	// 검색 키워드 없음
	public Page<CommunityBoard> pagingList(int pageNum, String sType, String sKeyword) {
		// 페이지 설정
		Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(Order.desc("id")));
		
		// 결과 담을 list 생성
		Page<CommunityBoard> pagingList = null;
		
		if(sType != null) {
			// 제목 + 내용 검색
			if(sType.equals("snc")) {
				// 페이지 설정
				// Direction.DESC, "id" 적용 안됨
				pageable = PageRequest.of(pageNum, 20);
				
				pagingList = communityBoardRepository.findBySbjOrCntContainsOrderByIdDesc(sKeyword, pageable);
				
			// 작성자 검색
			} else if(sType.equals("usr")) {
				pagingList = communityBoardRepository.findByUsernameContains(sKeyword, pageable);
				
			// 제목 검색
			} else if(sType.equals("sbj")) {
				pagingList = communityBoardRepository.findBySubjectContains(sKeyword, pageable);
				
			// 내용 검색
			} else if(sType.equals("cnt")) {
				pagingList = communityBoardRepository.findByContentContains(sKeyword, pageable);
			}
			
		// 기본
		} else {
			pagingList = communityBoardRepository.findAll(pageable);
		}
		
		return pagingList;
	}

	// 게시글 등록
	public int write(MultipartFile multipartFile, CommunityBoard cbEntity, String username) {
		// 검색에 이용할 사용자 이름도 저장
		User user = loadUser(username);
		cbEntity.setUsername(username);
		cbEntity.setUser(user);
		
		cbEntity.setCreatedAt(LocalDateTime.now());
		
		// Id값 가져오기
		communityBoardRepository.save(cbEntity).getId();
		
		CommunityFile cfEntity = new CommunityFile();
		
		// 파일 없으면 넘어가기
		if(!multipartFile.isEmpty()) {
			try {
				cfEntity = uploadFile(multipartFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			cfEntity.setCommunityBoard(cbEntity);
			
			communityFileRepository.save(cfEntity);
		}
		
		return 1;
	}

	// 게시글 조회
	@Transactional
	public List<CommunityBoard> view(Long id) {
		// 게시글 SELETE
		List<CommunityBoard> list = new ArrayList<>();
		CommunityBoard cbEntity = communityBoardRepository.findById(id).orElse(null);
		
		// 조회수 증가
		if(cbEntity != null) {
			cbEntity.setViewCnt(cbEntity.getViewCnt() + 1);
			communityBoardRepository.save(cbEntity);
			list.add(cbEntity);
		}
		
		System.out.println("커뮤니티 보드 : " + cbEntity);
		
		return list;
	}

	// 게시글 SELECT
	public List<CommunityBoard> selectById(Long id) {
		List<CommunityBoard> list = new ArrayList<>();
		list.add(communityBoardRepository.findById(id).orElse(null));
		
		return list;
	}

	// 게시글 수정
	public int update(MultipartFile multipartFile, CommunityBoard cbEntity, Long removeFileId) {
		int cnt = 0;
		
		CommunityFile cfEntity = new CommunityFile();
		
		if(removeFileId != null) {
			communityFileRepository.deleteById(removeFileId);
		}
		
		// 파일 없으면 넘어가기
		if(!multipartFile.isEmpty()) {
			try {
				cfEntity = uploadFile(multipartFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			cfEntity.setCommunityBoard(cbEntity);
			
			communityFileRepository.save(cfEntity);
		}
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbEntity.getId()).orElse(null);
		if(communityBoard != null) {
			communityBoard.setSubject(cbEntity.getSubject());
			communityBoard.setContent(cbEntity.getContent());
			communityBoardRepository.save(communityBoard);
			
			cnt = 1;
		}
		
		return cnt;
	}

	// 게시글 삭제
	public int delete(Long id) {
		communityBoardRepository.deleteById(id);
		
		return 1;
	}

	// 좋아요 ajax
	public Map<String, Object> likeToggle(Long cbId, String username) {
		int likes = 0;
		int toggle = 1;
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		
		User user = loadUser(username);
		
		List<CommunityLike> communityLike = new ArrayList<>();
		communityLike = communityBoard.getCommunityLikes();
		
		CommunityLike clEntity = new CommunityLike();
		
		if(communityLikeRepository.findByUserAndCommunityBoard(user, communityBoard) == null) {
			clEntity.setCommunityBoard(communityBoard);
			clEntity.setUser(user);
			clEntity.setToggle(toggle);
			communityLikeRepository.save(clEntity);
		} else {
			clEntity = communityLikeRepository.findByUserAndCommunityBoard(user, communityBoard);
			
			if(clEntity.getToggle() == 1) {
				toggle = 0;
			} else {
				toggle = 1;
			}
			
			clEntity.setToggle(toggle);
			communityLikeRepository.save(clEntity);
		}
		
		for(int i = 0; i < communityLike.size(); i++) {
			if(communityLike.get(i).getToggle() == 1) {
				likes++;
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("likes", likes);
		map.put("toggle", toggle);
		
		return map;
	}
	
	// 좋아요 수 ajax
	public Map<String, Object> likeLoad(Long cbId, String username) {
		int likes = 0;
		int toggle = 0;
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		
		User user = loadUser(username);
		
		List<CommunityLike> communityLike = new ArrayList<>();
		communityLike = communityBoard.getCommunityLikes();
		
		if(communityLikeRepository.findByUserAndCommunityBoard(user, communityBoard) != null) {
			toggle = communityLikeRepository.findByUserAndCommunityBoard(user, communityBoard).getToggle();
		}
		
		// toggle이 1일 때 likes에 추가
		for(int i = 0; i < communityLike.size(); i++) {
			if(communityLike.get(i).getToggle() == 1) {
				likes++;
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("likes", likes);
		map.put("toggle", toggle);
		
		return map;
	}
	
	// 좋아요 수 비로그인
	public int likeLoadLogout(Long cbId) {
		int likes = 0;
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		
		List<CommunityLike> communityLike = new ArrayList<>();
		communityLike = communityBoard.getCommunityLikes();
		
		// toggle이 1일 때 likes에 추가
		for(int i = 0; i < communityLike.size(); i++) {
			if(communityLike.get(i).getToggle() == 1) {
				likes++;
			}
		}
		
		return likes;
	}
	
	// 댓글 등록
	public int commentInsert(CommunityComment ccEntity, Long cbId, String username) {
		User user = loadUser(username);
		ccEntity.setUser(user);
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		ccEntity.setCommunityBoard(communityBoard);
		
		ccEntity.setCreatedAt(LocalDateTime.now());
		
		communityCommentRepository.save(ccEntity);
		
		return 1;
	}
	
	// 댓글 삭제
	public int commentDelete(Long id) {
		communityCommentRepository.deleteById(id);
		
		return 1;
	}
	
	// 댓글 삭제 후 돌아갈 페이지
	public List<CommunityBoard> selectByCcId(Long id) {
		List<CommunityBoard> list = new ArrayList<>();
		
		CommunityBoard communityBoard = communityCommentRepository.findById(id).get().getCommunityBoard();
		list.add(communityBoard);
		
		return list;
	}
	
	// 첨부파일 저장
	public CommunityFile uploadFile(MultipartFile multipartFile) throws Exception {		
		String originalName = multipartFile.getOriginalFilename();
		
		// 저장 폴더
		// application.properties에서 설정함
		String filePath = System.getProperty("user.dir") + "/src/main/resources/static/commuUpload/";
		
		// 확장자
		String ext = originalName.substring(originalName.lastIndexOf(".") + 1);
		
		// 저장할 파일 이름 변경
		String uuidName = UUID.randomUUID().toString().replaceAll("-", "");
		String fileName = uuidName + "." + ext;
		
		
		CommunityFile cfEntity = new CommunityFile();
		
		// 파일 경로
		File dest = new File(filePath + fileName);
		
		multipartFile.transferTo(dest);
		
		cfEntity.setOriginalName(originalName);
		cfEntity.setFileName(fileName);
		
		return cfEntity;
	}
	
}
