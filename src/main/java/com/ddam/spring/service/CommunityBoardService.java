package com.ddam.spring.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import com.ddam.spring.repository.CommunityBoardRepository;
import com.ddam.spring.repository.CommunityCommentRepository;
import com.ddam.spring.repository.CommunityFileRepository;
import com.ddam.spring.repository.CommunityLikeRepository;

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
	
//	private UserRepository userRepository;
//	
//	@Autowired
//	public void setUserRepository(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
	
	public CommunityBoardService() {
		System.out.println("CommunityBoardService() 생성");
	}
	
	
	// 유저 정보 가져오기
//	public User loadUser(String username) {
//		User user = userRepository.findByUsername(username);
//		
//		return user;
//	}

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
	public int write(MultipartFile multipartFile, CommunityBoard cbEntity) {
//		User user = loadUser(username);
		
		// 검색에 이용할 사용자 이름도 저장
//		cbEntity.setUsername(user.getUsername());
//		cbEntity.setCreatedAt(LocalDateTime.now());
//		cbEntity.setUser(user);
		
//		if(!multipartFile.isEmpty()) {
//			
//		}
		
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
	public int update(MultipartFile multipartFile, CommunityBoard cbEntity) {
		int cnt = 0;
		
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
	
	// 게시글 조회 시 좋아요 수
	public int viewLikes(Long id) {
		int likes = 0;
		
		CommunityBoard communityBoard = communityBoardRepository.findById(id).orElse(null);
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
	
	// 게시글 조회 시 좋아요 여부
	public int toggleValue(Long id) {
		int toggle = 0;
		
		CommunityBoard communityBoard = communityBoardRepository.findById(id).orElse(null);
		List<CommunityLike> communityLike = new ArrayList<>();
		communityLike = communityBoard.getCommunityLikes();
		
		for(int i = 0; i < communityLike.size(); i++) {
			// 기존 좋아요 유저 정보와 현재 유저 정보 일치하는지 확인
//			if(communityLike.get(i).getUser().equals(user)) {
//				// 일치하면 토글값 확인 후 변경
//				if(communityLike.get(i).getToggle() == 0) {
//					communityLike.get(i).setToggle(1);
//					
//					toggle = 1;
//				} else {
//					communityLike.get(i).setToggle(0);
//				}
//			}
		}
		
		return toggle;
	}

	// 좋아요
	public void likeToggle(Long cbId) {
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		CommunityLike clEntity = new CommunityLike();
		clEntity.setCommunityBoard(communityBoard);
		clEntity.setToggle(1);
		
		communityLikeRepository.save(clEntity);
	}
	
	// 좋아요 업데이트
	public List<CommunityLike> likeUpdate(Long cbId) {
		List<CommunityLike> communityLike = new ArrayList<>();
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		
		communityLike = communityBoard.getCommunityLikes();
		
		return communityLike;
	}
	
	// 댓글 등록
	@Transactional
	public int commentInsert(CommunityComment ccEntity, Long cbId) {
		int cnt = 0;
		
//		User user = loadUser(username);
		
//		ccEntity.setCreatedAt(LocalDateTime.now());
//		ccEntity.setUser(user);
		
		CommunityBoard communityBoard = communityBoardRepository.findById(cbId).orElse(null);
		
		if(communityBoard != null) {
			ccEntity.setCreatedAt(LocalDateTime.now());
			ccEntity.setCommunityBoard(communityBoard);
			communityCommentRepository.save(ccEntity);
			
			cnt = 1;
		}
		
		return cnt;
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