package com.sebastian.inventory_management.event.Supplier;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.model.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SupplierEvent {
    
    private final Supplier supplier;
    private final ActionType actionType;
}
