package com.sebastian.inventory_management.event.Order;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.model.Order;

/**
 * Evento de domínio para Order.
 * Estende BaseEvent utilizando Generics para reutilização de código.
 */
public class OrderEvent extends BaseEvent<Order> {

    public OrderEvent(Order order, ActionType actionType) {
        super(order, actionType);
    }

    @Override
    public Long getEntityId() {
        return getEntity().getId();
    }

    @Override
    public String getEntityDescription() {
        return "Orden #" + getEntity().getOrderNumber();
    }

    @Override
    public String getEntityTypePrefix() {
        return "ORDER";
    }
}
