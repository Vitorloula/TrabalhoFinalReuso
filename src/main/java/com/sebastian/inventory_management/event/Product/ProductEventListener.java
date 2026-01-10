package com.sebastian.inventory_management.event.Product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.event.base.AbstractEventListener;
import com.sebastian.inventory_management.service.IActivityLogService;

/**
 * Listener para eventos de Product.
 * Utiliza Template Method herdado de AbstractEventListener.
 */
@Component
public class ProductEventListener extends AbstractEventListener<ProductEvent> {

    public ProductEventListener(IActivityLogService activityLogService) {
        super(activityLogService);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductEvent(ProductEvent event) {
        handleEvent(event);
    }

    @Override
    protected String getTitleCreated() {
        return "Nuevo Producto Creado";
    }

    @Override
    protected String getTitleUpdated() {
        return "Producto Actualizado";
    }

    @Override
    protected String getTitleDeleted() {
        return "Producto Eliminado";
    }

    @Override
    protected String getDescriptionCreated(ProductEvent event) {
        return event.getEntityDescription() + " para Proveedor " + event.getEntity().getSupplier().getName();
    }
}
