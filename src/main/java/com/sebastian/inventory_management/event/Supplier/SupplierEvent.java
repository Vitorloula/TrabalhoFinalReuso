package com.sebastian.inventory_management.event.Supplier;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.model.Supplier;

/**
 * Evento de domínio para Supplier.
 * Estende BaseEvent utilizando Generics para reutilização de código.
 */
public class SupplierEvent extends BaseEvent<Supplier> {

    public SupplierEvent(Supplier supplier, ActionType actionType) {
        super(supplier, actionType);
    }

    @Override
    public Long getEntityId() {
        return getEntity().getId();
    }

    @Override
    public String getEntityDescription() {
        return "Proveedor #" + getEntityId() + " - " + getEntity().getName();
    }

    @Override
    public String getEntityTypePrefix() {
        return "SUPPLIER";
    }
}
