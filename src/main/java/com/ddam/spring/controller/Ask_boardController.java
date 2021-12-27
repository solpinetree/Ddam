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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ddam.spring.domain.Ask_board;
import com.ddam.spring.domain.Ask_file;
import com.ddam.spring.service.Ask_boardService;
import com.ddam.spring.service.Ask_fileService;

@Controller
public class Ask_boardController {
	
	@Autowired
	private Ask_boardService ask_boardService;
	
	@Autowired
	private Ask_fileService ask_fileService;
	
	@Autowired
	public void setAsk_boardService(Ask_boardService ask_boardService) {
		this.ask_boardService = ask_boardService;
	}

	@Autowired
	public void setAsk_fileService(Ask_fileService ask_fileService) {
		this.ask_fileService = ask_fileService;
	}

	public Ask_boardController() {
		System.out.println("Ask_boardController() 생성");
	}
	
	@RequestMapping("/ask/write")
	public void write() {}


	@PostMapping("/ask/writeOk")
	public void writeOk(Ask_board dto, Model model,
			HttpServletRequest request, @RequestPart MultipartFile files) {
		System.out.println("writeOk 진입");
		
		//제목,내용
		Long cnt = ask_boardService.saveAsk_board(dto);
		model.addAttribute("result", cnt);
		model.addAttribute("dto", dto);
		
		//파일첨부
		Ask_file file = new Ask_file();
		
		String sourceFileName = files.getOriginalFilename(); 
		String destinationFileName = sourceFileName;
		
		System.out.println(sourceFileName);
		
		file.setFilename(destinationFileName);
		file.setOriginalname(sourceFileName);
		file.setBoard(dto);
		file.setAbid(dto.getAbid());
		ask_fileService.saveAsk_file(file);
		
		dto.getAskFiles().add(file);
		
		System.out.println(dto.getAskFiles().get(0).getFilename());
		

		
		model.addAttribute("file", file);
	}
	
	@GetMapping("/admin/ask/write")
	public void adminwrite(@RequestParam Long abid, Model model) {
		System.out.println(abid);
		model.addAttribute("ask_boardlist", ask_boardService.selectByAbid_Admin(abid));
		model.addAttribute("ask_filelist", ask_fileService.selectByAfid_Admin(abid));	
	}
	
	@PostMapping("/admin/ask/writeOk")
	public void writeOk(@ModelAttribute("dto") Ask_board ask_board, Model model) {
//		Long cnt = ask_boardService.saveAsk_board_admin(dto);
//		model.addAttribute("result", cnt);
//		model.addAttribute("dto", dto);
		model.addAttribute("ask_boardresult", ask_boardService.update_admin(ask_board));
	}
	
	@GetMapping("/ask/view")
	public void view(Long abid, Model model) {
		model.addAttribute("ask_boardlist", ask_boardService.viewByAbid(abid));
		model.addAttribute("ask_filelist", ask_fileService.viewByAfid(abid));
	}

	@RequestMapping("/ask/list")
	public void list(Model model) {
		System.out.println("list 진입");
		List<Ask_board> ask_boardlist = ask_boardService.list();
//		for(int i = 0; i < ask_boardlist.size(); i++) {
//			System.out.println(ask_boardlist.get(i).toString());
//		}
		model.addAttribute("ask_boardlist", ask_boardService.list());
	}
	
	@GetMapping("/ask/update")
	public void update(int abid, Model model) {
		model.addAttribute("ask_boardlist", ask_boardService.selectByAbid(abid));
		model.addAttribute("ask_filelist", ask_fileService.selectByAfid(abid));
	}
	
	@PostMapping("/ask/updateOk")
	public void updateOk(@ModelAttribute("dto") Ask_board ask_board, Model model, @RequestPart MultipartFile files) {
		String originalFilename = files.getOriginalFilename();
		model.addAttribute("ask_boardresult", ask_boardService.update(ask_board, originalFilename));
	}
	
	@GetMapping("/ask/deleteOk")
	public void deleteOk(int abid, Model model) {
		model.addAttribute("ask_boardresult", ask_boardService.deleteByUid(abid));
	}

}
