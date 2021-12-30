package com.ddam.spring.controller;


import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

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
	

	
	
	
	
	

}

	
	
	
	


