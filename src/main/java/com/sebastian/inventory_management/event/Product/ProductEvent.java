package com.sebastian.inventory_management.event.Product;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.model.Product;

/**
 * Evento de domínio para Product.
 * Estende BaseEvent utilizando Generics para reutilização de código.
 */
public class ProductEvent extends BaseEvent<Product> {

    public ProductEvent(Product product, ActionType actionType) {
        super(product, actionType);
    }

    @Override
    public Long getEntityId() {
        return getEntity().getId();
    }

    @Override
    public String getEntityDescription() {
        return "Producto #" + getEntityId() + " - " + getEntity().getName();
    }

    @Override
    public String getEntityTypePrefix() {
        return "PRODUCT";
    }
}
