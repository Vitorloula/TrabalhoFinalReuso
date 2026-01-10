package com.sebastian.inventory_management.event.Supplier;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.event.base.AbstractEventListener;
import com.sebastian.inventory_management.service.IActivityLogService;

/**
 * Listener para eventos de Supplier.
 * Utiliza Template Method herdado de AbstractEventListener.
 */
@Component
public class SupplierEventListener extends AbstractEventListener<SupplierEvent> {

    public SupplierEventListener(IActivityLogService activityLogService) {
        super(activityLogService);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSupplierEvent(SupplierEvent event) {
        handleEvent(event);
    }

    @Override
    protected String getTitleCreated() {
        return "Nuevo Proveedor Creado";
    }

    @Override
    protected String getTitleUpdated() {
        return "Proveedor Actualizado";
    }

    @Override
    protected String getTitleDeleted() {
        return "Proveedor Eliminado";
    }
}
