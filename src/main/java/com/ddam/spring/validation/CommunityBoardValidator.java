package com.ddam.spring.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ddam.spring.domain.CommunityBoard;

public class CommunityBoardValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return CommunityBoard.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CommunityBoard communityBoard = (CommunityBoard)target;
		
		// 게시글 제목 공백
		String subject = communityBoard.getSubject();
		
		if(subject.length() < 2 || subject.trim().isEmpty()) {
			errors.rejectValue("subject", "subjectError");
		}
		
		// 게시글 내용 공백
		String content = communityBoard.getContent();
		
		if(content == null || content.trim().isEmpty()) {
			errors.rejectValue("content", "contentError");
		}
	}

}
