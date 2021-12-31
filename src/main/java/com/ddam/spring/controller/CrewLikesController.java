package com.ddam.spring.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.LikesRepository;
import com.ddam.spring.service.LikesService;

@Controller
public class CrewLikesController {
	
	@Autowired
	LikesService likesService;
	
	@Autowired
	LikesRepository likesRepository;
	
	@Autowired
	CrewRepository crewRepository;

	@PostMapping("/likes/update")
	@ResponseBody
	public 	void update(Model model, @RequestParam Map<String, Object> paramMap) {

		long userId = Long.parseLong((String) paramMap.get("userId"));
		long crewId = Long.parseLong((String) paramMap.get("crewId"));
		
		
		// 동일 크루에 대한 이전 좋아요 여부 검색
		int result = likesService.find(userId,crewId);
		
		if(result ==0) {	//좋아요 누르지 않은 상태라면
			likesService.save(userId,crewId);
		}else {	// 이미 좋아요를 누른 상태라면
			likesService.delete(userId,crewId);
		}
	}
	
	
	@PostMapping("/likes/count")
	public @ResponseBody Map<String, Integer> count(Model model,@RequestParam Map<String, Object> paramMap) {
		
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
		
		Map<String, Integer> res = new HashMap<>();
		
		// 해당 크루의 좋아요 수를 구함
		int count = likesRepository.countByToCrewId(crewId);
//		Long count = crewRepository.findById(crewId).getLikesCount();
		
		res.put("count", count);
		
		return res;
	}
}
