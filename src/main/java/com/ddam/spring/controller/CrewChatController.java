package com.ddam.spring.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddam.spring.domain.CrewChat;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.CrewChatService;

@Controller
public class CrewChatController {
	
	@Autowired
	CrewChatService crewChatService;
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/crew/message/{id}")
	public String message(@PathVariable("id") int id, Model model) throws Exception {
		model.addAttribute("page_id", id);

		return "/crew/message";
	}

	@PostMapping("/chat/insert")
	@ResponseBody
	private int chat_insert(@RequestParam long writerId, @RequestParam String message) throws Exception {
		crewChatService.save(writerId, message);
		return 1;
	}

	@PostMapping("/chat/list/{id}")
	@ResponseBody
	private List<CrewChat> chatlist(@PathVariable int id, HttpServletRequest request) throws Exception {

		
		List<CrewChat> chatting = crewChatService.findAll();
		CrewChat ccc = new CrewChat();
		Collections.sort(chatting, ccc); // 역순으로 정렬하기

		return chatting;
	}
}
