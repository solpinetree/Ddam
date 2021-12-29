package com.ddam.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ddam.spring.domain.Crew;
import com.ddam.spring.domain.FollowRequest;
import com.ddam.spring.domain.Meetup;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.FollowRequestRepository;
import com.ddam.spring.repository.MeetupRepository;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.FollowRequestService;
import com.ddam.spring.service.FollowService;
import com.ddam.spring.validation.CrewValidator;

@Controller
@RequestMapping("/crew")
public class CrewController {
	

	@Autowired
	private CrewRepository crewRepository;
	
	@Autowired
	private FollowRequestService followRequestService;
	
	@Autowired
	private FollowRequestRepository followRequestRepository;
	
	@Autowired
	private FollowService followService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MeetupRepository meetupRepository;
	
	// 이 컨트롤러 의 handler 에서 폼 데이터를 바인딩할때 검증하는 객체 지정
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CrewValidator());
	}
	
	// area 지역 
	List<String> areaList = Arrays.asList(
			new String[]{"서울", "인천", "대전", "세종", "경기", "충북", "충남", "강원", "광주", "부산",
					"대구", "울산","전북", "전남","경북", "경남","제주"});
	// + 글로벌 ..
	
	// 운동 종류
	List<String> category = Arrays.asList(
			new String[] {"러닝" ,"조깅","자전거","등산","배드민턴","테니스","탁구","축구","농구","스키","요가",
					"족구","배구", "소프트볼"});
	
	/**
	 * 크루 모집 페이지
	 */
	@GetMapping("/crews")
	public String crews(Model model) {
		List<Crew> crewList = crewRepository.findAll();
		model.addAttribute("crewList", crewList);
		return "crew/crews";
	}

	/**
	 * 크루 싱글 페이지(한 크루의 상세 페이지)
	 */
	@GetMapping("/crew-detail/{cid}")
	public String crew_single(@PathVariable("cid") long cid,  Model model, HttpServletRequest request) {
		
		
    	HttpSession session = request.getSession();
    	User user = (User)session.getAttribute("user");
    	
    	System.out.println("crew-detail: " +user);   	
    	if(user!=null) {
		
	    	// 지금 로그인한 유저와 크루와의 관계
	    	String crewRole = followService.find(user.getId(), cid);
	    	model.addAttribute("crewRole", crewRole);
    	}
    	
    	
    	Crew crew = crewRepository.findById(cid);
    	model.addAttribute("crew", crew);
    	
    	List<FollowRequest> requests= followRequestRepository.findByToCrewId(cid);
    	model.addAttribute("requests",requests);
    	
    	List<User> members = followService.findMembers(cid);
    	model.addAttribute("members", members);
    	
    	List<Meetup> meetupLists = meetupRepository.findByCrewId(cid);
    	model.addAttribute( "meetupLists", meetupLists);
		
		return "crew/crewdetail";
	}

	/**
	 * 크루 개설 페이지
	 */
	@GetMapping("/newcrew")
	public String crew_enroll(Crew crew, Model model) {
		model.addAttribute("areaList", areaList);
		model.addAttribute("category", category);
		return "crew/newcrew";
	}
	
	
	

	@PostMapping("/crew-writeOk")
	public String writeOk(@RequestParam("file") MultipartFile file, @Valid Crew crew, 
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IllegalStateException, IOException {

		
		// 크루 썸네일을 입력 안 한 경우
		if(crew.getFileName()==null&&file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errFile", "썸네일은 필수입니다.");
		}else if(!file.isEmpty()){
			
			System.out.println("파일 입력 됨");
			
			ServletContext context = request.getServletContext();
			String saveDirectory = context.getRealPath("crewphoto");
			System.out.println("saveDirectory: " + saveDirectory);
			
			File f = new File(saveDirectory);
			if(!f.exists()) {
				if(f.mkdir()) {
					System.out.println("폴더 생성 성공!");
				} else {
					System.out.println("폴더 생성 실패");
				}
			} else {
				System.out.println("폴더가 이미 존재합니다");
			}
			
			
			// 크루 썸네일이 입력된 경우 
//			String path = System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/";
			String path = saveDirectory;
			UUID uuid = UUID.randomUUID();
			String fileName = uuid+"_"+file.getOriginalFilename();
			File saveFile = new File(path, fileName);
			System.out.println("파일 저장 경로: "+ saveFile.getAbsolutePath());
	        file.transferTo(saveFile);
			
			crew.setFileOriginName(file.getOriginalFilename());
			crew.setFileName(fileName);
			crew.setFilePath("/crewphoto/" + fileName);
		}

		// 크루 입력 내용 유효성 검사
		if(result.hasErrors() || crew.getFileName()==null) {
			redirectAttributes.addFlashAttribute("crew", crew);
			redirectAttributes.addFlashAttribute("fileName", crew.getFileName());
			redirectAttributes.addFlashAttribute("fileOriginName", crew.getFileOriginName());
			redirectAttributes.addFlashAttribute("filePath", crew.getFilePath());
			showErrors(result, redirectAttributes);
			return "redirect:/crew/newcrew";			
		}
		

		// 유효성 검사 통과 후 
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		User user = userRepository.findByUsername(username);
		
		System.out.println(user + " crewAdmin user 정보");

		crew.setMemberCount(1L);
		crew.setLikesCount(0L);
		crew.setCrewAdmin(user);
		
		System.out.println(crew);
		
		crewRepository.save(crew);

		// 파일 업로드할 때 딜레이가 있어서....
		try {
			Thread.sleep((long) 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "redirect:/crew/crews";
	}
	
	/**
	 * @param id : 크루 id
	 * 크루 정보 수정
	 */
	@RequestMapping("/update/{id}")
	public String crewUpdate(@PathVariable("id") long id, Crew crew, Model model) {
		model.addAttribute("crew", crewRepository.findById(id));
		model.addAttribute("cid", id);	
		model.addAttribute("areaList", areaList);
		model.addAttribute("category", category);
		return "crew/crew-update";
	}
	
	@PostMapping("/crew-updateOk")
	public String updateOk(@RequestParam("file") MultipartFile file, @Valid Crew crew, 
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws IllegalStateException, IOException{
	
		
		// 크루 썸네일을 수정한 경우
		if(!file.isEmpty()){
			
			System.out.println("파일 입력 됨");
			// 기존 파일 삭제
			File prefile = new File(System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/" + crew.getFilePath());
			prefile.delete();
			
			
			String path = System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/";
			UUID uuid = UUID.randomUUID();
			String fileName = uuid+"_"+file.getOriginalFilename();
			File saveFile = new File(path + fileName);
			System.out.println("파일 저장 경로: "+saveFile);
	        file.transferTo(saveFile);
			
			crew.setFileOriginName(file.getOriginalFilename());
			crew.setFileName(fileName);
			crew.setFilePath("/crewphoto/" + fileName);
		}

		// 크루 입력 내용 유효성 검사
		if(result.hasErrors() || crew.getFileName()==null) {
			redirectAttributes.addFlashAttribute("crew", crew);
			showErrors(result, redirectAttributes);
			return "redirect:/crew/update/" + crew.getId();			
		}
		

		System.out.println(crew);
		
		crewRepository.save(crew);

		// 파일 업로드할 때 딜레이가 있어서....
		try {
			Thread.sleep((long) 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "crew/crew-detail/"+crew.getId();
	}
	
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		crewRepository.deleteById((long) id);
		return "redirect:/crew/crews";
	}
	
	
	/**
	 * 크루 관리 페이지(크루장만 접근 가능) 
	 */
	@GetMapping("/manage/{id}")
	private String crewmanage(@PathVariable("id") long id, Model model){
		System.out.println("crew manage 진입 ");
		List<User> crewMembers = followService.findMembers(id);
		model.addAttribute("crewMembers", crewMembers );
		return "crew/manage";
	}


	
	/**
	 *  팔로우 요청이 왔을 때
	 */
	@RequestMapping("/follow/request/{cid}")
	public String request(@PathVariable("cid") long cid, Model model) throws Exception {
		
		model.addAttribute("cid", cid);
		return "crew/follow-request";
	}
	
	/**
	 *  팔로우 요청 완료
	 */
	@PostMapping("/follow/request-ok")
	public String requestOk(@RequestParam String info, @RequestParam long cid, @RequestParam long uid,
			RedirectAttributes redirectAttributes) {
		
		FollowRequest followRequest = new FollowRequest(); 
    	followRequest.setInfo(info);
		
		
    	if(followRequestService.check(uid, cid)==true) {
    		User user = userRepository.findById(uid);
    		Crew crew = crewRepository.findById(cid);
    		followRequest.setFromUser(user);
    		followRequest.setToCrew(crew);
    		followRequestService.requestSave(followRequest);
//    		crew.getRequests().add(user);
    	}
    	
    	redirectAttributes.addFlashAttribute("cid", cid);
    	
		return "crew/request-ok";
	}
	
	
	/**
	 *  언팔로우 요청이 왔을 때(크루 탈퇴)
	 */
	@RequestMapping("/unfollow/{cid}")
	public String unfollow(@PathVariable("cid") long cid, HttpServletRequest request) throws Exception {
		
    	HttpSession session = request.getSession();
    	User user = (User)session.getAttribute("sessionedUser");

//    	crewRepository.findById(cid).getMembers().remove(user);
    	
		followService.deleteByFromUserIdAndToCrewId(user.getId(), cid);

		return "redirect:/crew/crew-detail/" + cid;
	}
	
	/**
	 *  크루 멤버 내보내기 (크루장이)
	 */
	@PostMapping("/deletemember")
	@ResponseBody
	public void deletemember(@RequestParam Map<String, Object> paramMap) {
		
		long memberId = Long.parseLong((String)paramMap.get("memberId"));
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
		
		
		// Crew의 List<User> members에서 삭제
//		crewRepository.findById(crewId).getMembers().remove(userRepository.findById(memberId));
		
		followService.deleteByFromUserIdAndToCrewId(memberId, crewId);
		
	}
	
	/**
	 *  팔로우 요청에 승인할 때(크루장이)
	 */
	@RequestMapping("/follow")
	@ResponseBody
	public void follow(Model model, @RequestParam Map<String, Object> paramMap) {
		
		long requestId = Long.parseLong((String)paramMap.get("requestId"));
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
    	
		followService.save(requestId, crewId);
		
    	List<User> requests= followRequestService.requests(crewId);
    	model.addAttribute("requests",requests);
    	
    	List<User> members = followService.findMembers(crewId);
    	model.addAttribute("members", members);
	}
	
	/**
	 *  팔로우 요청에 거절할 때(크루장이)
	 */
	@RequestMapping("/reject")
	@ResponseBody
	public void reject(@RequestParam Map<String, Object> paramMap) {
		
		long requestId = Long.parseLong((String)paramMap.get("requestId"));
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
    	
		followRequestRepository.deleteByFromUserIdAndToCrewId(requestId, crewId);
	}
	
	
	/**
	 *  크루장이 크루 멤버 명단 확인할 때
	 *  cors 정책 : 같은 도메인 내에서는 안 막아도 됨.
	 */
	@PostMapping("/member")
	@ResponseBody
	public Object memberList(HttpServletRequest request, HttpServletResponse response){
		long cid = Long.parseLong(request.getParameter("cid"));
		List<User> crewMembers = followService.findMembers(cid);
		
		Map<String, List<User>> result = new HashMap<>();
		
		result.put("members", crewMembers);
		
		return result;
	}
	
	
	/**
	 * crew/newcrew에서 validation 에러 발생시 전달할 에러 메시지 설정한느 메소드
	 * @param errors
	 * @param redirectAttributes
	 */
	public void showErrors(Errors errors, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			List<FieldError> errList = errors.getFieldErrors();

			for (FieldError err : errList) {
				String code = err.getCode();
				switch (code) {
				case "emptyName":
					redirectAttributes.addFlashAttribute("errName", "크루 이름을 입력해주세요.");
					break;
				case "emptyArea":
					redirectAttributes.addFlashAttribute("errArea", "크루가 주로 활동할 지역을 입력해주세요.");
					break;
				case "emptyCategory":
					redirectAttributes.addFlashAttribute("errCategory", "크루의 운동 종목을 입력해주세요.");
					break;
				case "emptyMemberLimit":
					redirectAttributes.addFlashAttribute("errMemberLimit", "멤버를 몇명까지 수용할지 입력해주세요.");
					break;
				case "emptyDescription":
					redirectAttributes.addFlashAttribute("errDescription", "크루의 목표와 함께 소개 인사를 적어주세요.");
					break;
				}				
			}
		}
	}
}