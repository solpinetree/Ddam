package com.ddam.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.service.CrewService;



@Controller
public class IndexController {
	
	@Autowired
	private CrewService crewService;

	@Autowired
	private CrewRepository crewRepository;
	
	
	/*
	 *  메인 페이지
	 * */
	@GetMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/crewdetail")
	public String crewdetail(Long id, Model model) {
		Crew crew = crewRepository.findById(id).orElse(null);
		return "crew/crewdetail";
	}
	
}

	
	
	
	


