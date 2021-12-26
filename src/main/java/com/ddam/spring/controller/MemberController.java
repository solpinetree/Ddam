package com.ddam.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddam.spring.domain.User;
import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

	@Autowired
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	
	private UserRepository userRepository;
	
  	public MemberController(UserRepository userRepository) {
  		this.userRepository = userRepository;
  	}
    
  	@GetMapping(value = "/join") 
  	public String join(Model model){
  		  model.addAttribute("UserFormDto", new UserFormDto()); 
  		  return "members/join"; 
  		  }
  	
 	@PostMapping("/joinOk")
 	public String joinOk(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {


 		if(bindingResult.hasErrors()){ 
 			
 			redirectAttributes.addFlashAttribute("username", userFormDto.getUsername());
 			redirectAttributes.addFlashAttribute("passowrd", userFormDto.getPassword());
 			redirectAttributes.addFlashAttribute("name", userFormDto.getName());
 			redirectAttributes.addFlashAttribute("gender", userFormDto.getGender());
 			redirectAttributes.addFlashAttribute("email", userFormDto.getEmail());
 			redirectAttributes.addFlashAttribute("phone", userFormDto.getPhone());
 			
 			return "members/join"; 
 			}
 		  
 		  try { 
	 		  User user = User.createUser(userFormDto, passwordEncoder);
	 		  System.out.println(user);
	 		  userService.saveUser(user); 
 		  } 
 		  catch (IllegalStateException e){
	 		 model.addAttribute("errorMessage", e.getMessage()); 
	 		 return "members/join"; 
 		 }
 		  
 		 return "redirect:/";
 	}
    
 	@RequestMapping("/auth")
 	@ResponseBody
 	public Authentication auth(HttpSession session) {
 		return SecurityContextHolder.getContext().getAuthentication();
 	}
	 
  	@GetMapping("/user/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
  	
  	@GetMapping(value = "/login")
    public String loginMember(){
        return "/members/login";
    }
  	
  	@PostMapping(value = "/loginOk")
    public String login(){
        return "redirect:/";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/members/login";
    }

}