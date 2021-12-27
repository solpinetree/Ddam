package com.ddam.spring.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  
	USER("ROLE_USER", "크루원"),
	MANGER("ROLE_MANAGER","크루장"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
	
}