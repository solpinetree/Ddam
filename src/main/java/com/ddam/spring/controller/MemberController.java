package com.ddam.spring.controller;

import javax.servlet.http.Cookie;
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
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddam.spring.domain.User;
import com.ddam.spring.dto.UserFormDto;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.UserService;
import com.ddam.spring.validation.CommunityBoardValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("UserFormDto", new UserFormDto());
		return "members/join";
	}

	@PostMapping("/join")
	public String join(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {

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
			redirectAttributes.addFlashAttribute("message", "회원가입 성공!");
			userService.saveUser(user);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "members/join";
		}

		return "redirect:/";
	}

	@GetMapping("/")
	public String home(@CurrentUser User user, Model model) {
		if (user != null) {
			model.addAttribute(user);
		}

		return "index";
	}

	@RequestMapping("/auth")
	@ResponseBody
	public Authentication auth(HttpSession session) {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response,
					SecurityContextHolder.getContext().getAuthentication());
		}
		return "redirect:/";
	}

	@GetMapping(value = "/login")
	public String loginMember() {
		return "/members/login";
	}

	// Model model => User user 로 바꿨습니다. 오류가 나서
	@PostMapping(value = "/loginOk")
	public String login(@Validated @ModelAttribute User user,
			String username, String password,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "expetion", required = false) String exception,
			BindingResult bindingResult,
			HttpServletResponse response,
			HttpSession session
			) {

		User user1 = userRepository.findByUsername(username);

		if(!username.equals(user1.getUsername())) {
			return "redirect:/members/login";
		}
		
		if(!password.equals(user1.getPassword())) {
			return "redirect:/members/login";
		}

		session.setAttribute("sessionedUser",user1);
		return "redirect:/";
	}

	@GetMapping(value = "/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
		return "/members/login";
	}
	

}