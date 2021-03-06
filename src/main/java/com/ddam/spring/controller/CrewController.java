package com.ddam.spring.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import com.ddam.spring.domain.Follow;
import com.ddam.spring.domain.FollowRequest;
import com.ddam.spring.domain.Meetup;
import com.ddam.spring.domain.Notification;
import com.ddam.spring.domain.User;
import com.ddam.spring.repository.CrewRepository;
import com.ddam.spring.repository.FollowRepository;
import com.ddam.spring.repository.FollowRequestRepository;
import com.ddam.spring.repository.MeetupRepository;
import com.ddam.spring.repository.UserRepository;
import com.ddam.spring.service.CrewService;
import com.ddam.spring.service.FollowRequestService;
import com.ddam.spring.service.FollowService;
import com.ddam.spring.service.NotificationService;
import com.ddam.spring.validation.CrewValidator;

@Controller
@RequestMapping("/crew")
public class CrewController {
	
	@Autowired
	CrewValidator crewValidator;

	@Autowired
	private CrewRepository crewRepository;
	
	@Autowired
	private CrewService crewService;
	
	@Autowired
	private FollowRequestService followRequestService;
	
	@Autowired
	private FollowRequestRepository followRequestRepository;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MeetupRepository meetupRepository;
	
	@Autowired
	private FollowRepository followRepository;
	
	// ??? ???????????? ??? handler ?????? ??? ???????????? ??????????????? ???????????? ?????? ??????
	@InitBinder("crewValidator")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(crewValidator);
	}
	
	// area ?????? 
	List<String> areaList = Arrays.asList(
			new String[]{"??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????", "??????",
					"??????", "??????","??????", "??????","??????", "??????","??????"});
	// + ????????? ..
	
	// ?????? ??????
	List<String> category = Arrays.asList(
			new String[] {"??????" ,"??????","?????????","??????","????????????","?????????","??????","??????","??????","??????","??????",
					"??????","??????", "????????????"});
	
	/**
	 * ?????? ?????? ?????????
	 */
	@GetMapping("/crews")
	public String crews(Model model, HttpServletRequest request) {
		List<Crew> crewList = crewRepository.findAll(Sort.by(Sort.Direction.DESC, "likesCount"));
		long userCount = userRepository.count();
		long meetupCount = meetupRepository.count();
		model.addAttribute("userCount", userCount);
		model.addAttribute("meetupCount", meetupCount);
		model.addAttribute("crewList", crewList);
		
		
    	HttpSession session = request.getSession();
    	String username = (String)session.getAttribute("username");
    	User user = userRepository.findByUsername(username);
    	
    	if(user!=null) {
    		model.addAttribute("user",user);
    	}
		
		
		return "crew/crews";
	}

	/**
	 * ?????? ?????? ?????????(??? ????????? ?????? ?????????)
	 */
	@GetMapping("/crew-detail/{cid}")
	public String crew_single(@PathVariable("cid") long cid,  Model model, HttpServletRequest request) {
		
		
    	HttpSession session = request.getSession();
    	String username = (String)session.getAttribute("username");
    	User user = userRepository.findByUsername(username);
    	
    	model.addAttribute("user", user);
    	
    	if(user!=null) {
		
	    	// ?????? ???????????? ????????? ???????????? ??????
	    	String crewRole = followService.find(user.getId(), cid);
	    	model.addAttribute("crewRole", crewRole);
	    	List<Follow> followlist = followRepository.findByFromUserId(user.getId());
			model.addAttribute("followList", followlist);
    	}
    	
    	
    	Crew crew = crewRepository.findById(cid);
    	model.addAttribute("crew", crew);
    	
    	List<FollowRequest> requests= followRequestRepository.findByToCrewId(cid);
    	model.addAttribute("requests",requests);
    	
    	List<User> members = followService.findMembers(cid);
    	model.addAttribute("members", members);
    	
    	List<Meetup> meetupLists = meetupRepository.findByCrewIdAndDatetimeGreaterThanOrderByDatetimeAsc(cid, LocalDateTime.now().minusDays(1L));
    	model.addAttribute( "meetupLists", meetupLists);
    	
    	List<Meetup> allmeetupLists = meetupRepository.findByCrewId(cid);
    	model.addAttribute("allmeetupLists", allmeetupLists);
    	
		return "crew/crewdetail";
	}

	/**
	 * ?????? ?????? ?????????
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

		
		// ?????? ???????????? ?????? ??? ??? ??????
		if(crew.getFileName()==null&&file.isEmpty()) {
			redirectAttributes.addFlashAttribute("errFile", "???????????? ???????????????.");
		}else if(!file.isEmpty()){
			
			System.out.println("?????? ?????? ???");
			
			// ?????? ???????????? ????????? ?????? 
			String path = System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/";
			UUID uuid = UUID.randomUUID();
			String fileName = uuid+"_"+file.getOriginalFilename();
			File saveFile = new File(path + fileName);
			System.out.println("?????? ?????? ??????: "+saveFile);
	        file.transferTo(saveFile);
			
			crew.setFileOriginName(file.getOriginalFilename());
			crew.setFileName(fileName);
			crew.setFilePath("/crewphoto/" + fileName);
		}

		// ?????? ?????? ?????? ????????? ??????
		if(result.hasErrors() || crew.getFileName()==null) {
			redirectAttributes.addFlashAttribute("crew", crew);
			redirectAttributes.addFlashAttribute("fileName", crew.getFileName());
			redirectAttributes.addFlashAttribute("fileOriginName", crew.getFileOriginName());
			redirectAttributes.addFlashAttribute("filePath", crew.getFilePath());
			showErrors(result, redirectAttributes);
			return "redirect:/crew/newcrew";			
		}
		

		// ????????? ?????? ?????? ??? 
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		User user = userRepository.findByUsername(username);
		
		System.out.println(user + " crewAdmin user ??????");

		crew.setMemberCount(1L);
		crew.setLikesCount(0L);
		crew.setCrewAdmin(user);
		
		System.out.println(crew);
		
		crewRepository.save(crew);

		// ?????? ???????????? ??? ???????????? ?????????....
		try {
			Thread.sleep((long) 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "redirect:/crew/crew-detail/"+crew.getId();
	}
	
	/**
	 * @param id : ?????? id
	 * ?????? ?????? ??????
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
	
		Crew precrew = crewService.findById(crew.getId());
		
		
		// ?????? ???????????? ????????? ??????
		if(!file.isEmpty()){
			
			// ?????? ?????? ??????
			File prefile = new File(System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/" + crew.getFilePath());
			prefile.delete();
			
			
			String path = System.getProperty("user.dir") +"/src/main/resources/static/crewphoto/";
			UUID uuid = UUID.randomUUID();
			String fileName = uuid+"_"+file.getOriginalFilename();
			File saveFile = new File(path + fileName);
			System.out.println("?????? ?????? ??????: "+saveFile);
	        file.transferTo(saveFile);
			
			precrew.setFileOriginName(file.getOriginalFilename());
			precrew.setFileName(fileName);
			precrew.setFilePath("/crewphoto/" + fileName);
		}
				
		precrew.setArea(crew.getArea());
		precrew.setCategory(crew.getCategory());
		precrew.setDescription(crew.getDescription());
		precrew.setName(crew.getName());
		
		crewService.save(precrew);

		// ?????? ???????????? ??? ???????????? ?????????....
		try {
			Thread.sleep((long) 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "redirect:/crew/crew-detail/"+crew.getId();
	}
	
	
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id) {
		crewService.deleteById((long) id);
		return "redirect:/crew/crews";
	}
	
	
	/**
	 * ?????? ?????? ?????????(???????????? ?????? ??????) 
	 */
	@GetMapping("/manage/{id}")
	private String crewmanage(@PathVariable("id") long id, Model model){
		System.out.println("crew manage ?????? ");
		List<User> crewMembers = followService.findMembers(id);
		model.addAttribute("crewMembers", crewMembers );
		return "crew/manage";
	}
	
	/**
	 * ?????? ????????? request ?????? ???
	 * @param paramMap - crewId??? ?????? Map
	 * @return	- ?????? ????????? request ?????? ??????
	 */
	@PostMapping("/countrequest")
	public @ResponseBody Map<String, Integer> countRequest(@RequestParam Map<String, Object> paramMap) {
		
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
		
		Map<String, Integer> res = new HashMap<>();
		
		// ?????? ????????? ?????? ?????? ??????
		int count = followRequestRepository.countByToCrewId(crewId);
		
		res.put("count", count);
		
		return res;
	}

	/**
	 * ?????? ???????????? ??? ????????? ???????????? ??????
	 * @param cid ?????? id
	 * @return ?????? ????????? FollowRequest ???????????? ??????
	 */
	@PostMapping("/requestlist/{cid}")
	public @ResponseBody List<FollowRequest> requestList(@PathVariable long cid) {
		
		List<FollowRequest> res = followRequestRepository.findAllByToCrewId(cid);
		return res;
		
	}
	
	/**
	 *  ????????? ????????? ?????? ???
	 */
	@RequestMapping("/follow/request/{cid}")
	public String request(@PathVariable("cid") long cid, Model model, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		User user = userRepository.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("cid", cid);
		return "crew/follow-request";
	}
	
	/**
	 *  ????????? ?????? ??????
	 */
	@PostMapping("/follow/request-ok")
	public String requestOk(@RequestParam String info, @RequestParam long cid, @RequestParam long uid,
			Model model, RedirectAttributes redirectAttributes) {
		
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
    	
//    	redirectAttributes.addFlashAttribute("cid", cid);
    	model.addAttribute("cid", cid);
    	
		return "crew/request-ok";
	}
	
	
	/**
	 *  ???????????? ????????? ?????? ???(?????? ??????)
	 */
	@RequestMapping("/unfollow/{cid}")
	public String unfollow(@PathVariable("cid") long cid, HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("username");
		User user = userRepository.findByUsername(username);
    	
//    	crewRepository.findById(cid).getMembers().remove(user);
    	
		followService.deleteByFromUserIdAndToCrewId(user.getId(), cid);
		
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setNoti(crewRepository.findById(cid).getName()+" ????????? ??????????????????.");
		notificationService.save(notification);	

		return "redirect:/crew/crew-detail/" + cid;
	}
	
	/**
	 *  ?????? ?????? ???????????? (????????????)
	 */
	@PostMapping("/deletemember")
	@ResponseBody
	public void deletemember(@RequestParam Map<String, Object> paramMap) {
		
		long memberId = Long.parseLong((String)paramMap.get("memberId"));
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
		
		
		Notification notification = new Notification();
		notification.setUser(userRepository.findById(memberId));
		notification.setNoti(crewRepository.findById(crewId).getName()+" ???????????? ?????????????????????.");
		notificationService.save(notification);	
		
		// Crew??? List<User> members?????? ??????
//		crewRepository.findById(crewId).getMembers().remove(userRepository.findById(memberId));
		
		followService.deleteByFromUserIdAndToCrewId(memberId, crewId);
	}
	
	/**
	 *  ????????? ????????? ????????? ???(????????????)
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
	 *  ????????? ????????? ????????? ???(????????????)
	 */
	@RequestMapping("/reject")
	@ResponseBody
	public void reject(@RequestParam Map<String, Object> paramMap) {
		
		long requestId = Long.parseLong((String)paramMap.get("requestId"));
		long crewId = Long.parseLong((String)paramMap.get("crewId"));
		Notification notification = new Notification();
		
		User user = userRepository.findById(requestId);
		Crew crew = crewRepository.findById(crewId);
		
		notification.setUser(user);
		notification.setNoti(crew.getName()+" ?????? ?????? ????????? ?????????????????????.");
		notificationService.save(notification);	
    	
		followRequestRepository.deleteByFromUserIdAndToCrewId(requestId, crewId);
	}
	
	
	/**
	 *  ???????????? ?????? ?????? ?????? ????????? ???
	 *  cors ?????? : ?????? ????????? ???????????? ??? ????????? ???.
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
	 * crew/newcrew?????? validation ?????? ????????? ????????? ?????? ????????? ???????????? ?????????
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
					redirectAttributes.addFlashAttribute("errName", "?????? ????????? ??????????????????.");
					break;
				case "emptyArea":
					redirectAttributes.addFlashAttribute("errArea", "????????? ?????? ????????? ????????? ??????????????????.");
					break;
				case "emptyCategory":
					redirectAttributes.addFlashAttribute("errCategory", "????????? ?????? ????????? ??????????????????.");
					break;
				case "emptyMemberLimit":
					redirectAttributes.addFlashAttribute("errMemberLimit", "????????? ???????????? ???????????? ??????????????????.");
					break;
				case "emptyDescription":
					redirectAttributes.addFlashAttribute("errDescription", "????????? ????????? ?????? ?????? ????????? ???????????????.");
					break;
				}				
			}
		}
	}
	
	
	
}
