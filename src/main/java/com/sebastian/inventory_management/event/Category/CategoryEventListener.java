package com.sebastian.inventory_management.event.Category;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.event.base.AbstractEventListener;
import com.sebastian.inventory_management.service.IActivityLogService;

/**
 * Listener para eventos de Category.
 * Utiliza Template Method herdado de AbstractEventListener.
 */
@Component
public class CategoryEventListener extends AbstractEventListener<CategoryEvent> {

    public CategoryEventListener(IActivityLogService activityLogService) {
        super(activityLogService);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCategoryEvent(CategoryEvent event) {
        handleEvent(event);
    }

    @Override
    protected String getTitleCreated() {
        return "Nueva Categoría Creada";
    }

    @Override
    protected String getTitleUpdated() {
        return "Categoría Actualizada";
    }

    @Override
    protected String getTitleDeleted() {
        return "Categoría Eliminada";
    }
}
