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
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
	
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
<<<<<<< Updated upstream

}
=======
	
	
	
	
}
>>>>>>> Stashed changes
