package com.ddam.spring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
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
import com.ddam.spring.validation.UserValidator;

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
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public MemberController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	// 회원가입 페이지
	@GetMapping("/join")
	public String join(Model model) {
		model.addAttribute("UserFormDto", new UserFormDto());
		return "members/join";
	}

	@PostMapping("/join")
	public String joinOk(@Valid UserFormDto userFormDto, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		// Validator 생성
		Validator validator = new UserValidator();
		validator.validate(userFormDto, bindingResult);

		// 유효성 검사
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
			User user = User.createUser(userFormDto, bCryptPasswordEncoder);
			redirectAttributes.addFlashAttribute("message", "회원가입 성공!");
			userService.saveUser(user);
			System.out.println(user + " insert 성공");
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "members/join";
		}

		return "redirect:/members/login";
	}

	// 로그인 페이지
	@GetMapping(value = "/login")
	public String loginMember() {
		System.out.println("GET: /login");
		System.out.println("");

		return "/members/login";
	}

	@PostMapping(value = "/loginOk")
	public String loginOk(@Validated @ModelAttribute User user, String username, String password,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "expetion", required = false) String exception, BindingResult bindingResult,
			HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		System.out.println("loginOk 확인");

		// 로그인 직전의 url 을 Session 에 기록
		String referer = request.getHeader("Referer");
		if (referer != null)
			request.getSession().setAttribute("url_prior_login", referer);

		session.setAttribute("sessionedUser", user.getUsername());

		// 현재 로그인한 user 정보 Authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedUser = (User) authentication.getPrincipal();
		if (loggedUser != null) {
			System.out.println("현재로그인 username : " + loggedUser.getUsername());
		}

		System.out.println("POST: /login");
		return "redirect:/";
	}

	// 마이페이지
	@RequestMapping("/mypage")
	public void mypage(Long username, User user) {

	}

	// 크루장페이지
	@RequestMapping("/manager")
	public void manager(Long username, User user) {

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

	// 검증 실패 시 에러 메시지 출력
	public void showErrors(Errors errors, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			List<FieldError> errorList = errors.getFieldErrors();

			for (FieldError error : errorList) {
				String code = error.getCode();

				if (code.equals("emptyName")) {
					redirectAttributes.addFlashAttribute("nameError", "※ 이름을 입력해주세요");
				} else if (code.equals("emptyUsername")) {
					redirectAttributes.addFlashAttribute("usernameError", "※ 닉네임을 입력해주세요");
				} else if (code.equals("emptyPassword")) {
					redirectAttributes.addFlashAttribute("passwordError", "※ 비밀번호를 입력해주세요");
				} else if (code.equals("emptyEmail")) {
					redirectAttributes.addFlashAttribute("emialError", "※ 이메일을 입력해주세요");
				}
			}
		}
	}

}