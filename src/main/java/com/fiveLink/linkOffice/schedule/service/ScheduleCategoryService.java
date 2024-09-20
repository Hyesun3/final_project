package com.fiveLink.linkOffice.schedule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiveLink.linkOffice.schedule.domain.ScheduleCategory;
import com.fiveLink.linkOffice.schedule.domain.ScheduleCategoryDto;
import com.fiveLink.linkOffice.schedule.repository.ScheduleCategoryRepository;
import com.fiveLink.linkOffice.schedule.repository.ScheduleRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduleCategoryService {
    private final ScheduleCategoryRepository scheduleCategoryRepository;
    private final ScheduleRepository scheduleRepository;
    
    @Autowired
    public ScheduleCategoryService(ScheduleCategoryRepository scheduleCategoryRepository, ScheduleRepository scheduleRepository) {
        this.scheduleCategoryRepository = scheduleCategoryRepository;
        this.scheduleRepository = scheduleRepository;
    }
  
    public List<ScheduleCategoryDto> getAllScheduleCategories() {
        List<ScheduleCategory> scheduleCategories = scheduleCategoryRepository.findAllByScheduleCategoryStatusOrderByScheduleCategoryNameAsc(0L);
        return scheduleCategories.stream()
                .map(ScheduleCategoryDto::toDto) 
                .collect(Collectors.toList());
    }
     
    public ScheduleCategoryDto getScheduleCategoryById(Long id) {
        return scheduleCategoryRepository.findById(id)
                .map(ScheduleCategoryDto::toDto)   
                .orElse(null);  
    }
    
    // 수정
    public void updateScheduleCategory(Long categoryId, String categoryName, String categoryColor, Long onlyAdmin) {
        ScheduleCategory category = scheduleCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        
        category.setScheduleCategoryName(categoryName);
        category.setScheduleCategoryColor(categoryColor);
        category.setScheduleCategoryAdmin(onlyAdmin);
        
        scheduleCategoryRepository.save(category);
    }
    
    // 삭제
    @Transactional
    public boolean deleteScheduleCategory(Long categoryId) {
        Optional<ScheduleCategory> categoryOpt = scheduleCategoryRepository.findById(categoryId);

        if (categoryOpt.isPresent()) {
            ScheduleCategory category = categoryOpt.get();
 
            long relatedSchedules = scheduleRepository.countByScheduleCategoryNoAndScheduleStatus(categoryId, 0L);

            // 카테고리 사용 중이면 삭제 불가
            if (relatedSchedules > 0) { 
                return false;
            } else { 
                category.setScheduleCategoryStatus(1L);
                scheduleCategoryRepository.save(category);
                return true;
            }
        }
        return false;
    }

    // 등록
    @Transactional
    public Map<String, String> addScheduleCategory(String scheduleCategoryName, String scheduleCategoryColor, Long onlyAdmin) {
        Map<String, String> response = new HashMap<>();

        // 중복 카테고리명  
        boolean isNameDuplicate = scheduleCategoryRepository.existsByScheduleCategoryNameAndScheduleCategoryStatus(scheduleCategoryName, 0L);
        if (isNameDuplicate) {
            response.put("name", "중복된 카테고리명이 존재합니다.");
        }

        // 중복 색상  
        boolean isColorDuplicate = scheduleCategoryRepository.existsByScheduleCategoryColorAndScheduleCategoryStatus(scheduleCategoryColor, 0L);
        if (isColorDuplicate) {
            response.put("color", "중복된 색상이 존재합니다.");
        }
 
        if (!isNameDuplicate) {
            if (!isColorDuplicate) {
                ScheduleCategory scheduleCategory = ScheduleCategory.builder()
                    .scheduleCategoryName(scheduleCategoryName)
                    .scheduleCategoryColor(scheduleCategoryColor)
                    .scheduleCategoryAdmin(onlyAdmin)
                    .scheduleCategoryStatus(0L)  
                    .build();

                scheduleCategoryRepository.save(scheduleCategory);
                response.put("success", "카테고리가 성공적으로 등록되었습니다.");
            }
        } 
        return response;  
    }



    
}
