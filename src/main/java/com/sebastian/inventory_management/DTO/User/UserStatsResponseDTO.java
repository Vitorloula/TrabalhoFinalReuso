package com.sebastian.inventory_management.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserStatsResponseDTO {
    private long totalUsers;
    private long totalAdmins;
    private long totalEmployees;
    private long totalUsersInLast30Days;
}
