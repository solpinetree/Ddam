package com.ddam.spring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ddam.spring.domain.Notice_board;
import com.ddam.spring.domain.Notice_file;
import com.ddam.spring.service.Notice_boardService;
import com.ddam.spring.service.Notice_fileService;

@Controller
public class Notice_boardController {
	
	private Notice_boardService notice_boardService;

	private Notice_fileService notice_fileService;
	
	@Autowired
	public void setNotice_boardService(Notice_boardService notice_boardService) {
		this.notice_boardService = notice_boardService;
	}

	@Autowired
	public void setNotice_fileService(Notice_fileService notice_fileService) {
		this.notice_fileService = notice_fileService;
	}

	public Notice_boardController() {
		System.out.println("Notice_boardController() 생성");
	}
	
	@RequestMapping("/admin/notice/write")
	public void write() {}


	@PostMapping("/admin/notice/writeOk")
	public void writeOk(Notice_board dto, Model model,
			HttpServletRequest request, @RequestPart MultipartFile files) {
		System.out.println("writeOk 진입");
		//제목,내용
		Long cnt = notice_boardService.saveNotice_board(dto);
		model.addAttribute("result", cnt);
		model.addAttribute("dto", dto);
		
		//파일첨부
		Notice_file file = new Notice_file();
		
		String sourceFileName = files.getOriginalFilename(); 
		String destinationFileName = sourceFileName;

		file.setFilename(destinationFileName);
		file.setOriginalname(sourceFileName);
		file.setBoard(dto);
		notice_fileService.saveNotice_file(file);
		model.addAttribute("file", file);
	}

	@GetMapping("admin/notice/view")
	public void admin_view(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.viewByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.viewByNfid(nbid));
	}
	
	@GetMapping("/notice/view")
	public void view(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.viewByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.viewByNfid(nbid));
	}

	@RequestMapping("/admin/notice/list")
	public void admin_list(Model model) {
		List<Notice_board> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
	}

	@RequestMapping("/notice/list")
	public void list(Model model) {
		List<Notice_board> notice_boardlist = notice_boardService.list();
		model.addAttribute("notice_boardlist", notice_boardService.list());
	}
	
	@GetMapping("/admin/notice/update")
	public void update(int nbid, Model model) {
		model.addAttribute("notice_boardlist", notice_boardService.selectByNbid(nbid));
		model.addAttribute("notice_filelist", notice_fileService.selectByNfid(nbid));
	}
	
	@PostMapping("/admin/notice/updateOk")
	public void updateOk(@ModelAttribute("dto") Notice_board notice_board, Model model, @RequestPart MultipartFile files) {
		String originalFilename = files.getOriginalFilename();
		model.addAttribute("notice_boardresult", notice_boardService.update(notice_board, originalFilename));
	}
	
	@GetMapping("/admin/notice/deleteOk")
	public void deleteOk(int nbid, Model model) {
		model.addAttribute("notice_boardresult", notice_boardService.deleteByUid(nbid));
	}

}
