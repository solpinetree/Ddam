package com.ddam.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.CommunityComment;
import com.ddam.spring.domain.CommunityLike;
import com.ddam.spring.service.CommunityBoardService;

@Controller
@RequestMapping("/community")
public class CommunityBoardController {
	
	private CommunityBoardService communityBoardService;
	
	@Autowired
	public void setCommunityBoardService(CommunityBoardService communityBoardService) {
		this.communityBoardService = communityBoardService;
	}

	public CommunityBoardController() {
		System.out.println("CommunityBoardController() 생성");
	}

	// list
	@RequestMapping("/list")
	public void list(@RequestParam(defaultValue = "0") int page ,String sType, String sKeyword, Model model) {
		Page<CommunityBoard> pagingList = communityBoardService.pagingList(page, sType, sKeyword);
		
		// 전체 페이지 수
		int totalPage = pagingList.getTotalPages();
		
		if(totalPage < 1) {
			totalPage = 1;
		}
		
		model.addAttribute("pagingList", pagingList);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("page", page);
		model.addAttribute("sType", sType);
		model.addAttribute("sKeyword", sKeyword);
	}
	
	// write
	@RequestMapping("/write")
	public void write() {
		
	}
	
	// writeOk
	@PostMapping("/writeOk")
	public void writeOk(@RequestParam("multipartFile") MultipartFile multipartFile, CommunityBoard cbEntity, Model model){
		model.addAttribute("cbEntity", cbEntity);
		model.addAttribute("result", communityBoardService.write(multipartFile, cbEntity));
	}
	
	// view
	@RequestMapping("/view")
	public void view(Long id, Model model) {
		model.addAttribute("list", communityBoardService.view(id));
	}
	
	// update
	@RequestMapping("/update")
	public void update(Long id, Model model) {
		model.addAttribute("list", communityBoardService.selectById(id));
	}
	
	// updateOk
	@PostMapping("/updateOk")
	public void updateOk(@RequestParam("multipartFile") MultipartFile multipartFile, CommunityBoard cbEntity, Model model) {
		model.addAttribute("cbEntity", cbEntity);
		model.addAttribute("result", communityBoardService.update(multipartFile, cbEntity));
	}
	
	// deleteOk
	@RequestMapping("/deleteOk")
	public void deleteOk(Long id, Model model) {
		model.addAttribute("result", communityBoardService.delete(id));
	}
	
	// likeInsert
	@PostMapping("/likeInsert")
	public void likeInsert(CommunityLike clEntity, Model model) {
		model.addAttribute("cbId", clEntity.getCommunityBoard().getId());
//		model.addAttribute("result", communityBoardService.likeToggle(clEntity));
	}
	
	// commentInsert
	@PostMapping("/commentInsert")
	public void commentInsert(CommunityComment ccEntity, Long cbId, Model model) {
		model.addAttribute("cbId", cbId);
		model.addAttribute("result", communityBoardService.commentInsert(ccEntity, cbId));
	}
	
	// commentDelete
	@RequestMapping("/commentDelete")
	public void commentDelete(Long id, Model model) {
		model.addAttribute("list", communityBoardService.selectByCcId(id));
		model.addAttribute("result", communityBoardService.commentDelete(id));
	}
	
}
