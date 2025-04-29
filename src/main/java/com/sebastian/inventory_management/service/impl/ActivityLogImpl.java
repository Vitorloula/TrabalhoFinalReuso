package com.sebastian.inventory_management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sebastian.inventory_management.DTO.ActivityLog.ActivityLogResponseDTO;
import com.sebastian.inventory_management.mapper.ActivityLogMapper;
import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.repository.ActivityLogRepository;
import com.sebastian.inventory_management.service.IActivityLogService;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ActivityLogImpl implements IActivityLogService{

    private ActivityLogRepository activityLogRepository;
    private ActivityLogMapper activityLogMapper;

    @Autowired
    public ActivityLogImpl(ActivityLogRepository activityLogRepository, ActivityLogMapper activityLogMapper) {
        this.activityLogRepository = activityLogRepository;
        this.activityLogMapper = activityLogMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityLogResponseDTO> getRecentActivities() {
        List<ActivityLog> activities = activityLogRepository.findTop5ByOrderByCreatedAtDesc();
        return activityLogMapper.toDTOList(activities);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ActivityLogResponseDTO saveActivityLog(ActivityLog activityLog) {
        ActivityLog savedActivityLog = activityLogRepository.save(activityLog);
        return activityLogMapper.toDTO(savedActivityLog);
    }
    
}
