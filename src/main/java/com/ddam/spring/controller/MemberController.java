package com.ddam.spring.controller;

import com.ddam.spring.domain.User;
import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.UserService;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

	private UserService userService;
	private PasswordEncoder passwordEncoder;
	
	private UserRepository userRepository;
	
  	public MemberController(UserRepository userRepository) {
  		this.userRepository = userRepository;
  	}

  	@GetMapping(value = "/join")
    public String join(Model model){
        model.addAttribute("UserFormDto", new UserFormDto());
        return "member/join";
    }

    @PostMapping(value = "/join")
    public String newMember(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/join";
        }

        try {
            User user = User.createUser(userFormDto, passwordEncoder);
            userService.saveUser(user);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }

        return "redirect:/";
    }
	 
  	@GetMapping("/user/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
  	
  	@GetMapping(value = "/login")
    public String loginMember(){
        return "/member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/login";
    }

}