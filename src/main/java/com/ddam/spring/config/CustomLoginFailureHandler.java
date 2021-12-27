package com.ddam.spring.config;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	private final String DEFAULT_FAILURE_URL = "/login?error=true";
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		System.out.println("### 로그인 실패 ###");
		
		String errorMessage = null;
		
		if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
			errorMessage = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해주세요";
		}
		//< account is disabled
		else if(exception instanceof DisabledException) {
			errorMessage = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
		}
		//< expired the credential
		else if(exception instanceof CredentialsExpiredException) {
			errorMessage = "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요.";
		}
		else {
			errorMessage = "알수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
		}
		
		//< set attributes
		request.setAttribute("errorMessage", errorMessage);		
		request.setAttribute("username", request.getParameter("username"));
		
		// forwarding
		request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);
		
	}

}














