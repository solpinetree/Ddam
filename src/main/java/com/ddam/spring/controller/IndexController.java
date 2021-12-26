package com.ddam.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
	
}

	
	
	
	


