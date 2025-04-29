package com.sebastian.inventory_management.event.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.model.Product;
import com.sebastian.inventory_management.service.IActivityLogService;

@Component
public class ProductEventListener {
    
    private IActivityLogService activityLogService;

    @Autowired
    public ProductEventListener(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductEvent(ProductEvent event) {
        Product product = event.getProduct();
        if (product != null && product.getId() != null) {
            ActivityLog log = new ActivityLog();

            switch (event.getActionType()) {
                case CREATED:
                    log.setType("PRODUCT_CREATED");
                    log.setTitle("Nuevo Producto Creado");
                    log.setDescription("Producto #" + product.getId() + " para Proveedor " + product.getSupplier().getName());
                    break;
                case UPDATED:
                    log.setType("PRODUCT_UPDATED");
                    log.setTitle("Producto Actualizado");
                    log.setDescription("Se actualizó el producto #" + product.getId());
                    break;
                case DELETED:
                    log.setType("PRODUCT_DELETED");
                    log.setTitle("Orden Eliminada");
                    log.setDescription("Se eliminó el producto #" + product.getId());
                    break;
            }

            activityLogService.saveActivityLog(log);
        }
    }
}
