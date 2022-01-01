package com.ddam.spring.config;


import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ddam.spring.domain.User;
import com.ddam.spring.repository.UserRepository;

//AuthenticationSuccessHandler(I)
//  └─ SavedRequestAwareAuthenticationSuccessHandler
// https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/web/authentication/SavedRequestAwareAuthenticationSuccessHandler.html

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepository;
	
	public CustomLoginSuccessHandler(String defaultTargetUrl) {
		// 로그인후 특별히 redirect 할 url 이 없는경우 기본적으로 rediret 할 url
		setDefaultTargetUrl(defaultTargetUrl);
	}

	// 로그인 성공후 수행할 동작
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		System.out.println("### 로그인 성공 ###");
		
		// 로그인 정보 출력
		
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		System.out.println("username: " + userDetails.getUsername());
		System.out.println("password: " + userDetails.getPassword());
//		List<String> roleNames = new ArrayList<>();
//		authentication.getAuthorities().forEach(authority -> {
//			roleNames.add(authority.getAuthority());
//		});
//		System.out.println("authorities: " + roleNames);  // 권한이름들
		
		// 로그인 시간을 세션에 저장하기
		LocalDateTime loginTime = LocalDateTime.now();
		System.out.println("로그인시간: " + loginTime);
		request.getSession().setAttribute("loginTime", loginTime);
		
		request.getSession().setAttribute("username", userDetails.getUsername());
		
		request.getSession().setAttribute("user", (User)userDetails);
		
		
		
		// 로그인 직전 url 로 redirect 하기...
		HttpSession session = request.getSession();
		if(session != null) {
			String redirectUrl = (String)session.getAttribute("url_prior_login");
			if(redirectUrl != null) {
				session.removeAttribute("url_prior_login");
				getRedirectStrategy().sendRedirect(request, response, redirectUrl);
			} else {
				super.onAuthenticationSuccess(request, response, authentication);		
			}
		} else {
			super.onAuthenticationSuccess(request, response, authentication);			
		}		
	}
}



















