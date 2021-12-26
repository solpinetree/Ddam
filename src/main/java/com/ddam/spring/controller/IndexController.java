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
	
	/**
	 * 	로그인 페이지 -- 여기서부터 테스트용 User
	 */
    @RequestMapping("/login")
 	public String login(User user) {
 		return "login";
 	}

    @PostMapping("/signIn")
    public String signIn(String username, HttpServletRequest request) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
        	HttpSession session = request.getSession();
        	session.setAttribute("user", user);
            return "loginOK";
        }
        return "loginFail";
    }
    
    @RequestMapping("/join")
    public String signup() {
        return "join";
    }
   
    @RequestMapping("/joinOk")
    public String joinOk(User user) {
    	userRepository.save(user);
        return "login";
    }
    
    @ResponseBody
    @RequestMapping("/status")
    public String status(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(session!=null) {
    		User user = (User)session.getAttribute("user");
    		return user.getUsername();
    	}
    	return null;
    }
    
    @RequestMapping("/signout")
    public String signout(HttpServletRequest request) {
    	HttpSession session = request.getSession();
    	if(session!=null) {
    		session.invalidate();
    	}
        return "join";
    }
    
    @RequestMapping("/meetup")
    public String meetup(HttpServletRequest request) {
        return "crew/meetup";
    }
    
}
