package com.ddam.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	
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
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/newForm")
	public String newForm() {
		return "newForm";
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