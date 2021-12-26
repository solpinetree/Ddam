package com.ddam.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddam.spring.service.Notice_fileService;

@Service
public class Notice_fileController {
	
	private Notice_fileService notice_fileService;
	
	@Autowired
	public void setNotice_fileService(Notice_fileService notice_fileService) {
		this.notice_fileService = notice_fileService;
	}

	public Notice_fileController() {
		System.out.println("Notice_fileController() 생성");
	}

}
