package com.ddam.spring.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping("/insert/{cid}")
	public String insert(@PathVariable long cid, Meetup meetup, Model model) {
		meetup.setCrew(crewRepository.findById(cid));
		model.addAttribute(meetup);
		return "crew/meetupCreate";
	}
	
	@PostMapping("/insertOk")
	public String insertOk(@RequestParam("crewId") long cid, @RequestParam("datetime2") String datetime2, Meetup meetup) throws ParseException {
		 SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");  
		 Date date1 = formatter6.parse(datetime2);
		meetup.setCrew(crewRepository.findById(cid));
		meetup.setDatetime(date1);
		Meetup res = meetupRepository.save(meetup);
		return "redirect:/crew/crew-detail/"+res.getCrew().getId();
	}
}
