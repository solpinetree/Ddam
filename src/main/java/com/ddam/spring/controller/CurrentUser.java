package com.ddam.spring.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지할 수 있도록 합니다.
@Target(ElementType.PARAMETER)      // 파라메타에만 붙을 수 있도록 합니다.
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : user")
//로그인 이전에는 anonymousUser라는 String 타입이고, 인증되었다면 user를 받을 수 있도록 합니다.
public @interface CurrentUser {
}