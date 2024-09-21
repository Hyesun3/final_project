package com.fiveLink.linkOffice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fiveLink.linkOffice.attendance.domain.AttendanceDto;
import com.fiveLink.linkOffice.attendance.service.AttendanceService;
import com.fiveLink.linkOffice.member.domain.MemberDto;
import com.fiveLink.linkOffice.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private final MemberService memberService;
	private final AttendanceService attendanceService;

	@Autowired
	public HomeController(MemberService memberService,
			AttendanceService attendanceService) {
		this.memberService = memberService;
		this.attendanceService = attendanceService;
		
	}

	@GetMapping("/login")
	public String loginPage(HttpSession session, Model model) {
	    String errorMsg = (String) session.getAttribute("error");
	    
	    if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            session.removeAttribute("error");
        }
		return "login";
	}

	@GetMapping("/pwchange")
	public String pwchangePage() {
		return "pwchange";
	}

	@GetMapping("/error")
	public String error() {
		return "error";
	}
	  
	@GetMapping({"/",""})
	public String home(HttpServletRequest request, Model model) {

			Long member_no = memberService.getLoggedInMemberNo();
			List<MemberDto> memberdto = memberService.getMembersByNo(member_no);

		    
		    // [박혜선] 현재 시간을 00:00:00 형태로 만들기
		    LocalTime now = LocalTime.now();
		    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		    String time = now.format(dtf);
		    
		    // [박혜선] 출퇴근 기록 조회
		    Long memberNo = memberdto.get(0).getMember_no();
		    LocalDate today = LocalDate.now();
		    AttendanceDto attendanceDto = attendanceService.findByMemberNoAndWorkDate(memberNo, today);
		    logger.info("AttendanceDto: {}", attendanceDto);
		    String isCheckedIn = "false";
	        String isCheckedOut = "false";
	        
	        if (attendanceDto != null) {
	            if (attendanceDto.getCheck_in_time() != null) {
	                isCheckedIn = "true";
	                model.addAttribute("checkInTime", attendanceDto.getCheck_in_time().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	            }
	            if (attendanceDto.getCheck_out_time() != null) {
	                isCheckedOut = "true";
	                model.addAttribute("checkOutTime", attendanceDto.getCheck_out_time().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	            }
	        }

		    //[전주영] 멤버 객체 전달
		    model.addAttribute("memberdto", memberdto);
		    
		    // [박혜선] 현재 시간 전달
		    model.addAttribute("time", time);
		    
		    // [박혜선] 출퇴근 여부 전달
		    model.addAttribute("isCheckedIn", isCheckedIn);
		    model.addAttribute("isCheckedOut", isCheckedOut);
		    
		    return "home";
		
	}



}