package com.fiveLink.linkOffice.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fiveLink.linkOffice.member.domain.MemberDto;
import com.fiveLink.linkOffice.member.service.MemberFileService;
import com.fiveLink.linkOffice.member.service.MemberService;
import com.fiveLink.linkOffice.organization.domain.DepartmentDto;

@Controller
public class MemberApiController {

	private final MemberService memberService;
	private final MemberFileService memberFileService;
	
	@Autowired
	public MemberApiController(MemberService memberService,MemberFileService memberFileService) {
		this.memberService = memberService;
		this.memberFileService = memberFileService;
	}
	
	// 전자결재 서명 등록
	@ResponseBody
	@PostMapping("/employee/member/digitalname/{member_no}")
	public Map<String, String> digitalnameUpdate( @PathVariable("member_no") Long memberNo,@RequestParam("signatureData") String signatureData) {
	    Map<String, String> response = new HashMap<>();
	    response.put("res_code", "404");
	    response.put("res_msg", "파일 등록 중 오류가 발생하였습니다.");
	    
	    MemberDto memberdto = memberService.selectMemberOne(memberNo); 
	     
	    try {
	        if (signatureData != null && !signatureData.isEmpty()) {
	            String newDigitalName = memberFileService.uploadDigital(signatureData);
	            if (newDigitalName != null) {
	                memberdto.setMember_ori_digital_img(signatureData);
	                memberdto.setMember_new_digital_img(newDigitalName);
	                
                    if(memberFileService.delete(memberdto.getMember_no()) > 0) {
                    	response.put("res_msg", "기존 파일 삭제 완료되었습니다.");
                    }else {
                    	response.put("res_msg", "기존 파일 삭제 중 오류가 발생되었습니다.");
                    }

	            }
	            
	            if (memberService.updateMemberDigital(memberdto) != null) {
                    response.put("res_code", "200");
                    response.put("res_msg", "파일 업로드가 완료되었습니다.");
                 
                }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("res_msg", "서버 오류가 발생하였습니다.");
	    }
	    return response;
	}
	
	// 비밀번호 확인
	@ResponseBody
	@PostMapping("/myedit/pwVerify/{member_no}")
	public Map<String,String> pwVerify(@PathVariable("member_no") Long memberNo,@RequestBody String pwVerify){
		 Map<String, String> response = new HashMap<>();
		    response.put("res_code", "404");
		    
		    MemberDto memberdto = memberService.selectMemberOne(memberNo); 
		 if(memberdto.getMember_pw().equals(pwVerify)) {
			 response.put("res_code", "200");
		 }
		 return response;
	}
	
	// 정보수정 
	@ResponseBody
	@PostMapping("/employee/member/myedit/{member_no}")
	public Map<String,String> profileUpdate(@PathVariable("member_no") Long memberNo,
            @RequestParam(name = "file", required = false) MultipartFile file, 
            @RequestParam(name = "roadAddress") String roadAddress,  
            @RequestParam(name = "detailAddress") String detailAddress, 
            @RequestParam(name = "newPassword") String newPassword){
		 Map<String, String> response = new HashMap<>();
		    response.put("res_code", "404");
		    response.put("res_msg", "파일 등록 중 오류가 발생하였습니다.");
		    
		    MemberDto memberdto = memberService.selectMemberOne(memberNo); 
		    
		    String newAdr = roadAddress + detailAddress;
		    if(!newAdr.isEmpty()) {
		    	memberdto.setMember_address(newAdr);
		    }
		    if(!newPassword.isEmpty()) {
		    	memberdto.setMember_pw(newPassword);
		    }
		    if(file != null && "".equals(file.getOriginalFilename()) == false) {
		    	String saveFileName = memberFileService.uploadProfile(file);
		    	if(saveFileName != null) {
		    		memberdto.setMember_ori_profile_img(file.getOriginalFilename());
		    		memberdto.setMember_new_profile_img(saveFileName);
		    		
		    		if(memberFileService.profileDelete(memberNo) > 0) {
		    			response.put("res_msg", "기존 파일이 삭제 되었습니다.");
		    		}else {
		    			response.put("res_msg", "기존 파일이 삭제 중 오류가 발생하었습니다.");
		    		}
		    	} else {
		    		response.put("res_msg", "파일 업로드 실패");
		    	}
		    	
		    }
		    
		    if(memberService.updateMemberProfile(memberdto) != null) {
		    	response.put("res_code", "200");
		    	response.put("res_msg", "정보 수정 성공하였습니다.");
		    }
		    return response;
	}
	
	
	// 관리자 사원 등록 
	@ResponseBody
	@PostMapping("/admin/member/create")
	public Map<String,String> memberCreate(@RequestParam("profile_image") MultipartFile profileImage,
            @RequestParam("name") String name,
            @RequestParam("national_number_front") String nationalNumberFront,
            @RequestParam("national_number_back") String nationalNumberBack,
            @RequestParam("hire_date") String hireDate,
            @RequestParam("mobile1") String mobile1,
            @RequestParam("mobile2") String mobile2,
            @RequestParam("mobile3") String mobile3,
            @RequestParam("internal1") String internal1,
            @RequestParam("internal2") String internal2,
            @RequestParam("internal3") String internal3,
            @RequestParam("department") String department,
            @RequestParam("position") String position){
		
		 Map<String, String> response = new HashMap<>();
		    response.put("res_code", "404");
		    response.put("res_msg", "사원 등록 중 오류가 발생하였습니다.");
		
		    MemberDto dto = new MemberDto();
		    
		try {
			
			String saveProfileName = memberFileService.uploadProfile(profileImage);
			if(saveProfileName != null) {
				dto.setMember_ori_profile_img(profileImage.getOriginalFilename());
				dto.setMember_new_profile_img(saveProfileName);
			}
			
			dto.setMember_name(name);
			
			dto.setMember_pw("1111");
			
			List<MemberDto> memberDtoList = memberService.getAllMembers();
			String national = nationalNumberFront + "-" + nationalNumberBack;
			
			for (MemberDto memberDto : memberDtoList) {
			    String memberNational = memberDto.getMember_national();
			    if (national.equals(memberNational)) {
			    	response.put("res_msg", "중복 주민번호를 가진 사원이 있습니다");
			    }else {
			    	dto.setMember_national(national);
			    }
			}
			
			dto.setMember_hire_date(hireDate);
			String mobile = mobile1 + "-" + mobile2 + "-" + mobile3;
			dto.setMember_mobile(mobile);
			
			String internal = internal1 + "-" + internal2 + "-" + internal3;
			dto.setMember_internal(internal);
			
			long departmentNo = Long.parseLong(department);
		    long positionNo = Long.parseLong(position);		
			dto.setDepartment_no(departmentNo);
			dto.setPosition_no(positionNo);
			
			if(memberService.createMember(dto) != null) {
				response.put("res_code", "200");
			    response.put("res_msg", "사원 등록을 성공하였습니다.");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

}
