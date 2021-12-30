package com.ddam.spring.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ddam.spring.domain.Crew;

@Component
public class CrewValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Crew.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Crew crew= (Crew)target;

		// 크루이름이 비어있을 때
		String name = crew.getName();
		if(name == null || name.trim().isEmpty()) {
			errors.rejectValue("name", "emptyName");
		}
		
		// 활동지역이 비어있을 때
		String area = crew.getArea();
		if(area == null || area.trim().isEmpty()) {
			errors.rejectValue("area", "emptyArea");
		}

		// 크루 운동 카테고리가 비어있을 때
		String category = crew.getCategory();
		if(category == null || category.trim().isEmpty()) {
			errors.rejectValue("category", "emptyCategory");
		}
		
		// 크루 인원 제한 정보가 비어있을 때
		Long memberLimit = crew.getMemberLimit();
		if(memberLimit==null) {
			errors.rejectValue("memberLimit", "emptyMemberLimit");
		}
		
		// 크루 목표 및 소개란이 비어있을 때
		String desc = crew.getDescription();
		if(desc == null || desc.trim().isEmpty()) {
			errors.rejectValue("description", "emptyDescription");
		}
	}

}
