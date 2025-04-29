package com.sebastian.inventory_management.event.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.model.Order;
import com.sebastian.inventory_management.service.IActivityLogService;

@Component
public class OrderEventListener {

    private IActivityLogService activityLogService;

    @Autowired
    public OrderEventListener(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderEvent(OrderEvent event) {
        Order order = event.getOrder();
        if (order != null && order.getId() != null) {
            ActivityLog log = new ActivityLog();

            switch (event.getActionType()) {
                case CREATED:
                    log.setType("ORDER_CREATED");
                    log.setTitle("Nueva Orden Creada");
                    log.setDescription("Orden #" + order.getOrderNumber() + " para Proveedor " + order.getSupplier().getName());
                    break;
                case UPDATED:
                    log.setType("ORDER_UPDATED");
                    log.setTitle("Orden Actualizada");
                    log.setDescription("Se actualizó la orden #" + order.getOrderNumber());
                    break;
                case DELETED:
                    log.setType("ORDER_DELETED");
                    log.setTitle("Orden Eliminada");
                    log.setDescription("Se eliminó la orden #" + order.getOrderNumber());
                    break;
            }

            activityLogService.saveActivityLog(log);
        }
    }
}
