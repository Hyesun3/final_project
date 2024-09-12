package com.fiveLink.linkOffice.document.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fiveLink.linkOffice.document.domain.DocumentFileDto;
import com.fiveLink.linkOffice.document.domain.DocumentFolderDto;
import com.fiveLink.linkOffice.document.service.DocumentFileService;
import com.fiveLink.linkOffice.document.service.DocumentFolderService;
import com.fiveLink.linkOffice.member.domain.MemberDto;
import com.fiveLink.linkOffice.member.service.MemberService;

@Controller
public class DocumentViewController {
	
	private final DocumentFolderService documentFolderService;
	private final MemberService memberService;
	private final DocumentFileService documentFileService;
	
	private static final Logger LOGGER
		= LoggerFactory.getLogger(DocumentViewController.class);
	
	@Autowired
	public DocumentViewController(DocumentFolderService documentFolderService,
			MemberService memberService,
			DocumentFileService documentFileService) {
		this.documentFolderService = documentFolderService;
		this.memberService = memberService;
		this.documentFileService = documentFileService;
	}
	
	// 개인 문서함 : 사원번호를 받아옴 
	@GetMapping("/employee/document/personal/{member_no}")
	public String documentPersonalPage(Model model,
			@PathVariable("member_no") Long memberNo
			) {		
		// memberDto 불러오기
	    List<MemberDto> memberdto = memberService.getMembersByNo(memberNo); 
		model.addAttribute("memberdto", memberdto);
		return "employee/document/personal";
	}
   // 개인 폴더 
   @GetMapping("/personal/folder")
   public ResponseEntity<List<DocumentFolderDto>> personalFolderList(
		   @RequestParam("memberNo") Long memberNo) throws IOException {
	   List<DocumentFolderDto> folderList = documentFolderService.selectPersonalFolderList(memberNo);
	   return ResponseEntity.ok(folderList);
   }
   
   // 개인 파일 
   @GetMapping("/personal/file")
   public ResponseEntity<List<DocumentFileDto>> selectPersonalfileList(
		   @RequestParam("memberNo") Long memberNo,
		   @RequestParam("folderId") Long folderId) throws IOException {
	   List<DocumentFileDto> fileList = documentFileService.selectPersonalfileList(memberNo , folderId);
       return ResponseEntity.ok(fileList);
   }
   
   // 개인 문서함 파일 사이즈 
   @GetMapping("/personal/fileSize")
   public ResponseEntity<Double> getAllFileSize(
		   @RequestParam("memberNo") Long memberNo) throws IOException{
	   double allFileSize = documentFileService.getAllFileSize(memberNo);
	   return ResponseEntity.ok(allFileSize);
   }
	
	// 부서 문서함 : 부서 번호를 받아옴 
	@GetMapping("/employee/document/department/{department_no}")
	public String documentDepartmentPage(Model model,
			@PathVariable("department_no") Long departmentNo,
			@RequestParam("memberNo") Long memberNo
			) {
		List<DocumentFolderDto> folderList = documentFolderService.selectDepartmentFolderList(departmentNo);
		// memberDto 불러오기
	    List<MemberDto> memberdto = memberService.getMembersByNo(memberNo);
	    
		model.addAttribute("folderList", folderList);
		model.addAttribute("memberdto", memberdto);
		return "employee/document/department";
	}
	
	// 사내 문서함 : 문서함 타입 = 2 로 지정해서 service에 보내줌 
	@GetMapping("/employee/document/company")
	public String documentCompanyPage(Model model, 
			@RequestParam("memberNo") Long memberNo) {
		Long document_box_type = 2L;
		List<DocumentFolderDto> folderList = documentFolderService.selectCompanyFolderList(document_box_type);
		
		// memberDto 불러오기
	    List<MemberDto> memberdto = memberService.getMembersByNo(memberNo);
	    
		model.addAttribute("folderList", folderList);
		model.addAttribute("memberdto", memberdto);
		return "employee/document/company";
	}
	
	// 휴지통 
	@GetMapping("/employee/document/bin/{member_no}")
	public String documentBinPage(Model model,
			@PathVariable("member_no") Long memberNo) {
		// memberDto 불러오기
	    List<MemberDto> memberdto = memberService.getMembersByNo(memberNo);
	    List<DocumentFileDto> fileList = documentFileService.documentBinList(memberNo);
	    
	    model.addAttribute("memberdto", memberdto);
	    model.addAttribute("fileList", fileList);
	    return "employee/document/bin";
		
	}

	
}