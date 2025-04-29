package com.sebastian.inventory_management.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.DTO.ActivityLog.ActivityLogResponseDTO;
import com.sebastian.inventory_management.service.IActivityLogService;

@RestController
@RequestMapping("/api/activities")
public class ActivityLogController {

    private IActivityLogService activityLogService;

    public ActivityLogController(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/recent")
    public List<ActivityLogResponseDTO> getRecentActivities() {
        return activityLogService.getRecentActivities();
    }
}