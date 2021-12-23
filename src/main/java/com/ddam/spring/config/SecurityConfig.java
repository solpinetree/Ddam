package com.ddam.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddam.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() // 
					.antMatchers("/**").permitAll() // 누구나 접근 허용
					.antMatchers("/user").hasRole("USER") // USER, ADMIN만 접근 가능
					.antMatchers("/manage").hasRole("MANAGE") // MANAGE만 접근 가능
					.antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
					.anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
				.and().formLogin() // 
					.loginPage("/login") // 로그인 페이지 링크
					.defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
				.and().logout() // 
					.logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
					.invalidateHttpSession(true) // 세션 날리기
					.deleteCookies("JSESSIONID")
		;
	}


}