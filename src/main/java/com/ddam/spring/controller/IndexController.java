package com.ddam.spring.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddam.spring.domain.User;
import com.ddam.spring.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
  	private UserRepository userRepository;
	
	
	/*
	 *  메인 페이지
	 * */
	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/list")
	public String list() {
		return "list";
	}
	
	@GetMapping("/recruitCrew")
	public String recruitCrew() {
		return "recruitCrew";
	}
	
	@GetMapping("/community")
	public String community() {
		return "community";
	}
	
	@GetMapping("/createCrew")
	public String createCrew() {
		return "createCrew";
	}

}

	
	
	
	


