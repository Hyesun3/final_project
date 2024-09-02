package com.fiveLink.linkOffice.vacation.controller;

import com.fiveLink.linkOffice.vacation.domain.VacationDto;
import com.fiveLink.linkOffice.vacation.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/vacation")
public class VacationFunctionController {

    private final VacationService vacationService;

    @Autowired
    public VacationFunctionController(VacationService vacationService){
        this.vacationService = vacationService;
    }

    @PostMapping("/addVacation")
    @ResponseBody
    public Map<String, String> addVacation(@ModelAttribute VacationDto dto){
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("res_code","404");
        resultMap.put("res_msg","휴가 생성 중 오류가 발생했습니다.");
        System.out.println(dto.getVacation_no());
        if(vacationService.addVacation(dto) > 0){
            resultMap.put("res_code","200");
            resultMap.put("res_msg","성공적으로 생성이 되었습니다.");
        }

        return resultMap;
    }
}
