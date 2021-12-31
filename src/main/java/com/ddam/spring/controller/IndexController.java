package com.ddam.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.Crew;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.service.CommunityBoardService;
import com.ddam.spring.service.CrewService;

@Controller
public class IndexController {

	@Autowired
	private CrewService crewService;

	@Autowired
	private CrewRepository crewRepository;

	private CommunityBoardService communityBoardService;

	@Autowired
	public void setCommunityBoardService(CommunityBoardService communityBoardService) {
		this.communityBoardService = communityBoardService;
	}
	/*
	 *  메인 페이지
	 * */
//	@GetMapping("/index")
//	public String index() {
//		return "index";
//	}
	
	@GetMapping(value = {"/"})
	public String crewList(Model model) {
		List<Crew> crews = crewRepository.findAll();
		model.addAttribute("crews",crews);
		return "index";
	}

	@GetMapping(value = {"/adminpage"})
	public void adminpage(@RequestParam(defaultValue = "0") int pageNum, String sType, String sKeyword, Model model) {
		Page<CommunityBoard> pagingList = communityBoardService.pagingList(pageNum, sType, sKeyword);
		List<Crew> crews = crewRepository.findAll();

		// 전체 페이지 수
		int totalPage = pagingList.getTotalPages();

		if (totalPage < 1) {
			totalPage = 1;
		}

		model.addAttribute("pagingList", pagingList);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("sType", sType);
		model.addAttribute("sKeyword", sKeyword);
		model.addAttribute("crews",crews);

	}

}
