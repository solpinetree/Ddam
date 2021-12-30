package com.ddam.spring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddam.spring.domain.CommunityBoard;
import com.ddam.spring.domain.CommunityComment;
import com.ddam.spring.domain.User;
import com.ddam.spring.service.CommunityBoardService;
import com.ddam.spring.validation.CommunityBoardValidator;

@Controller
@RequestMapping("/community")
public class CommunityBoardController {
	
	private CommunityBoardService communityBoardService;
	
	@Autowired
	public void setCommunityBoardService(CommunityBoardService communityBoardService) {
		this.communityBoardService = communityBoardService;
	}

	public CommunityBoardController() {
		System.out.println("CommunityBoardController() 생성");
	}

	// list
	@RequestMapping("/list")
	public void list(@RequestParam(defaultValue = "0") int pageNum ,String sType, String sKeyword, Model model) {
		Page<CommunityBoard> pagingList = communityBoardService.pagingList(pageNum, sType, sKeyword);
		
		// 전체 페이지 수
		int totalPage = pagingList.getTotalPages();
		
		if(totalPage < 1) {
			totalPage = 1;
		}
		
		model.addAttribute("pagingList", pagingList);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("sType", sType);
		model.addAttribute("sKeyword", sKeyword);
	}
	
	// write
	@RequestMapping("/write")
	public void write() {
		
	}
	
	// writeOk
	@PostMapping("/writeOk")
	public String writeOk(@RequestParam("multipartFile") MultipartFile multipartFile, CommunityBoard cbEntity
			, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		String page = "community/writeOk";
		
		// session에서 username 정보 가져오기
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		
		// Validator 생성
		Validator validator = new CommunityBoardValidator();
		validator.validate(cbEntity, bindingResult);
		
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("reSubject", cbEntity.getSubject());
			redirectAttributes.addFlashAttribute("reContent", cbEntity.getContent());
			showErrors(bindingResult, redirectAttributes);
			
			page = "redirect:/community/write";
		} else {
			model.addAttribute("cbEntity", cbEntity);
			model.addAttribute("result", communityBoardService.write(multipartFile, cbEntity, username));
		}
		
		return page;
	}
	
	// view
	@RequestMapping("/view")
	public void view(Long id, Model model) {
		model.addAttribute("list", communityBoardService.view(id));
		model.addAttribute("likes", communityBoardService.likeLoadLogout(id));
	}
	
	// update
	@RequestMapping("/update")
	public void update(Long id, Model model) {
		model.addAttribute("list", communityBoardService.selectById(id));
	}
	
	// updateOk
	@PostMapping("/updateOk")
	public String updateOk(@RequestParam("multipartFile") MultipartFile multipartFile, CommunityBoard cbEntity, Long removeFileId
			, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		
		String page = "community/updateOk";
		
		// Validator 생성
		Validator validator = new CommunityBoardValidator();
		validator.validate(cbEntity, bindingResult);
		
		if(bindingResult.hasErrors()) {
			showErrors(bindingResult, redirectAttributes);
			
			page = "redirect:/community/update?id=" + cbEntity.getId();
		} else {
			model.addAttribute("cbEntity", cbEntity);
			model.addAttribute("result", communityBoardService.update(multipartFile, cbEntity, removeFileId));
		}
		
		return page;
	}
	
	// deleteOk
	@RequestMapping("/deleteOk")
	public void deleteOk(Long id, Model model) {
		model.addAttribute("result", communityBoardService.delete(id));
	}
	
	
	// 좋아요 기능
	// likeView
	// view 진입 시
	@ResponseBody
	@RequestMapping("/likeView")
	public JSONObject likeView(Long cbId, HttpServletRequest request) {
		// session에서 username 정보 가져오기
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		
		Map<String, Object> map = new HashMap<>();
		
		map = communityBoardService.likeLoad(cbId, username);
		
		JSONObject jsonObj = new JSONObject(map);
		
		return jsonObj;
	}
	
	// likeToggle
	// 좋아요 클릭 시
	@ResponseBody
	@RequestMapping("/likeToggle")
	public JSONObject likeToggle(Long cbId, HttpServletRequest request) {
		// session에서 username 정보 가져오기
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		
		Map<String, Object> map = new HashMap<>();
		
		map = communityBoardService.likeToggle(cbId, username);
		
		JSONObject jsonObj = new JSONObject(map);
		
		return jsonObj;
	}
	
	
	// 댓글 관련 기능
	// commentInsert
	@PostMapping("/commentInsert")
	public void commentInsert(CommunityComment ccEntity, Long cbId, Model model, HttpServletRequest request) {
		// session에서 username 정보 가져오기
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		
		model.addAttribute("cbId", cbId);
		model.addAttribute("result", communityBoardService.commentInsert(ccEntity, cbId, username));
	}
	
	// commentDelete
	@RequestMapping("/commentDelete")
	public void commentDelete(Long id, Model model) {
		model.addAttribute("list", communityBoardService.selectByCcId(id));
		model.addAttribute("result", communityBoardService.commentDelete(id));
	}
	
	
	// 바인딩 기능
	// 바인딩 검증 객체 지정
	// list에서 오류나서 필요한 곳에 각각 생성하는걸로...
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.setValidator(new CommunityBoardValidator());
//	}
	
	// 검증 실패 시 에러 메시지 출력
	public void showErrors(Errors errors, RedirectAttributes redirectAttributes) {
		if(errors.hasErrors()) {
			List<FieldError> errorList = errors.getFieldErrors();
			
			for(FieldError error : errorList) {
				String code = error.getCode();
				
				if(code.equals("subjectError")) {
					redirectAttributes.addFlashAttribute("subjectError", "※ 제목을 2글자 이상 입력해주세요");
				} else if(code.equals("contentError")) {
					redirectAttributes.addFlashAttribute("contentError", "※ 내용을 입력해주세요");
				}
			}
		}
	}
	
}
