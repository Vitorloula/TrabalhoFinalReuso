package com.sebastian.inventory_management.event.Category;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.model.Category;

/**
 * Evento de domínio para Category.
 * Estende BaseEvent utilizando Generics para reutilização de código.
 */
public class CategoryEvent extends BaseEvent<Category> {

    public CategoryEvent(Category category, ActionType actionType) {
        super(category, actionType);
    }

    @Override
    public Long getEntityId() {
        return getEntity().getId();
    }

    @Override
    public String getEntityDescription() {
        return "Categoría #" + getEntityId() + " - " + getEntity().getName();
    }

    @Override
    public String getEntityTypePrefix() {
        return "CATEGORY";
    }
}
