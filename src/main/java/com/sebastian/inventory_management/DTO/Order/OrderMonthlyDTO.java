package com.sebastian.inventory_management.DTO.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMonthlyDTO {
    private String name;
    private Long value;
}

