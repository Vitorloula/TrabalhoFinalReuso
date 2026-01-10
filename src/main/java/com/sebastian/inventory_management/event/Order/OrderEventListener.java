package com.sebastian.inventory_management.event.Order;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.event.base.AbstractEventListener;
import com.sebastian.inventory_management.service.IActivityLogService;

/**
 * Listener para eventos de Order.
 * Utiliza Template Method herdado de AbstractEventListener.
 */
@Component
public class OrderEventListener extends AbstractEventListener<OrderEvent> {

    public OrderEventListener(IActivityLogService activityLogService) {
        super(activityLogService);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        handleEvent(event);
    }

    @Override
    protected String getTitleCreated() {
        return "Nueva Orden Creada";
    }

    @Override
    protected String getTitleUpdated() {
        return "Orden Actualizada";
    }

    @Override
    protected String getTitleDeleted() {
        return "Orden Eliminada";
    }

    @Override
    protected String getDescriptionCreated(OrderEvent event) {
        return event.getEntityDescription() + " para Proveedor " + event.getEntity().getSupplier().getName();
    }
}
