package com.sebastian.inventory_management.event.Order;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderEvent {
    
    private final Order order;
    private final ActionType actionType;

}
