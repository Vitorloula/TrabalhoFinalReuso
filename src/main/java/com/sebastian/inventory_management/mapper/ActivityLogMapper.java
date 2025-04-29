package com.sebastian.inventory_management.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.sebastian.inventory_management.DTO.ActivityLog.ActivityLogResponseDTO;
import com.sebastian.inventory_management.model.ActivityLog;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

    ActivityLogResponseDTO toDTO(ActivityLog category);
    List<ActivityLogResponseDTO> toDTOList(List<ActivityLog> activityLogs);
}
