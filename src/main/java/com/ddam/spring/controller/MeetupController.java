package com.ddam.spring.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ddam.spring.domain.Meetup;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.MeetupRepository;

@Controller
@RequestMapping("/crew/meetup")
public class MeetupController {

	@Autowired
	CrewRepository crewRepository;
	
	@Autowired
	MeetupRepository meetupRepository;
	
	
	/**
	 * 크루 모임 추가하는 페이지
	 * @param cid   크루 id
	 * @param meetup 	미팅 엔터티	
	 * @param model
	 * @return
	 */
	@RequestMapping("/insert/{cid}")
	public String insert(@PathVariable long cid, Meetup meetup, Model model) {
		meetup.setCrew(crewRepository.findById(cid));
		model.addAttribute(meetup);
		return "crew/meetupCreate";
	}
	
	/**
	 *  크루 모임 추가 완료 처리하는 핸들러
	 * @param cid
	 * @param datetime2	모임 날
	 * @param meetup
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/insertOk")
	public String insertOk(@RequestParam("crewId") long cid, 
			@RequestParam("datetime2") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime datetime2,
			Meetup meetup) throws ParseException {
		meetup.setCrew(crewRepository.findById(cid));
		meetup.setDatetime(datetime2);
		Meetup res = meetupRepository.save(meetup);
		return "redirect:/crew/crew-detail/"+res.getCrew().getId();
	}
	
	@RequestMapping("list")
	public String meetupList(Model model) {
		
		model.addAttribute("meetupLists", meetupRepository.findByDatetimeGreaterThanOrderByDatetimeAsc(LocalDateTime.now().minusDays(1L)));
		
		return "crew/allmeetup";
	}
}
