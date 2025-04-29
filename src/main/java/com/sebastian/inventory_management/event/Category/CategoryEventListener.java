package com.sebastian.inventory_management.event.Category;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.model.Category;
import com.sebastian.inventory_management.service.IActivityLogService;

@Component
public class CategoryEventListener {
    
    private IActivityLogService activityLogService;

    public CategoryEventListener(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCategoryEvent(CategoryEvent event) {
        Category category = event.getCategory();
        if (category != null && category.getId() != null) {
            ActivityLog log = new ActivityLog();

            switch (event.getActionType()) {
                case CREATED:
                    log.setType("CATEGORY_CREATED");
                    log.setTitle("Nueva Categoría Creada");
                    log.setDescription("Categoría #" + category.getId() + " con el nombre de  " + category.getName());
                    break;
                case UPDATED:
                    log.setType("CATEGORY_UPDATED");
                    log.setTitle("Categoría Actualizada");
                    log.setDescription("Se actualizó la categoría #" + category.getId() + " con el nuevo nombre de  " + category.getName());
                    break;
                case DELETED:
                    log.setType("CATEGORY_DELETED");
                    log.setTitle("Categoría Eliminada");
                    log.setDescription("Se eliminó la categoría #" + category.getId() + " con el nombre de  " + category.getName());
                    break;
            }

            activityLogService.saveActivityLog(log);
        }
    }
}
