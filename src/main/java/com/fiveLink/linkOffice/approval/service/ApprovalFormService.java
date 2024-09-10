package com.fiveLink.linkOffice.approval.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiveLink.linkOffice.approval.domain.ApprovalForm;
import com.fiveLink.linkOffice.approval.domain.ApprovalFormDto;
import com.fiveLink.linkOffice.approval.repository.ApprovalFormRepository;

@Service
public class ApprovalFormService {
    private final ApprovalFormRepository approvalFormRepository;

    public ApprovalFormService(ApprovalFormRepository approvalFormRepository) {
        this.approvalFormRepository = approvalFormRepository;
    }
    
    public Page<ApprovalFormDto> getAllApprovalForms(Pageable pageable, ApprovalFormDto searchdto) {
    	
        Page<ApprovalForm> forms = null;
        
        String searchText = searchdto.getSearch_text();
        if(searchText != null && "".equals(searchText) == false) {
        	forms = approvalFormRepository.findByaprovalFormTitleContaining(searchText, pageable, 1L);
        } else {
        	forms = approvalFormRepository.findAllByApprovalFormStatusNot(pageable,1L);
        }
        
        List<ApprovalFormDto> approvalFormDtoList = new ArrayList<ApprovalFormDto>();
        for(ApprovalForm af : forms) {
        	ApprovalFormDto dto = af.toDto();
        	approvalFormDtoList.add(dto);
        }
        
        
        return new PageImpl<>(approvalFormDtoList, pageable, forms.getTotalElements());
    }

    private ApprovalFormDto convertToDto(ApprovalForm form) {
        return ApprovalFormDto.builder()
                .approval_form_no(form.getApprovalFormNo())
                .approval_form_title(form.getApprovalFormTitle())
                .approval_form_content(form.getApprovalFormContent())
                .approval_form_create_date(form.getApprovalFormCreateDate())
                .approval_form_status(form.getApprovalFormStatus())
                .build();
    }
    
}