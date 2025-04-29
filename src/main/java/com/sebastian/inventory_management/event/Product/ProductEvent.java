package com.sebastian.inventory_management.event.Product;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductEvent {

    private final Product product;
    private final ActionType actionType;
}
