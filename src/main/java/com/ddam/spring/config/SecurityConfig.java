package com.ddam.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddam.spring.service.UserService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@EnableWebSecurity	// spring security를 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() // HttpServletRequest 요청 URL에 따라 접근 권한을 설정
					.antMatchers("/**").permitAll() // 누구나 접근 허용
					.antMatchers("/manage").hasRole("MANAGE") // MANAGE만 접근 가능
					.antMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 접근 가능
					.antMatchers("/h2-console/**").permitAll() 
					.anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
					
				.and().formLogin() // 
					.loginPage("/members/login") // 로그인 페이지 링크
					.loginProcessingUrl("/members/loginOk")
					.defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
					.successHandler(new CustomLoginSuccessHandler("/"))
					.failureHandler(new CustomLoginFailureHandler())
					
					.permitAll()
				.and().logout() // 
					.logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
					.invalidateHttpSession(true) // 세션 날리기
					.deleteCookies("JSESSIONID")	// 쿠키 제거
					.logoutSuccessHandler(new CustomLogoutSuccessHandler())
				.and()
	                .csrf() 
	                .ignoringAntMatchers("/h2-console/**").disable()
	                .httpBasic()
		;
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

//        auth.userDetailsService(userService);
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
	

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	// Security 무시하기 
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/h2-console/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }
   
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    
}