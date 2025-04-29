package com.sebastian.inventory_management.event.Category;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CategoryEvent {
    
    private final Category category;
    private final ActionType actionType;

}

