package com.sebastian.inventory_management.service;

import java.util.List;

import com.sebastian.inventory_management.DTO.ActivityLog.ActivityLogResponseDTO;
import com.sebastian.inventory_management.model.ActivityLog;

public interface IActivityLogService {

    List<ActivityLogResponseDTO> getRecentActivities();
    ActivityLogResponseDTO saveActivityLog(ActivityLog activityLog);
}
