package com.sebastian.inventory_management.event.Supplier;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.model.Supplier;
import com.sebastian.inventory_management.service.IActivityLogService;

@Component
public class SupplierEventListener {

    private IActivityLogService activityLogService;

    @Autowired
    public SupplierEventListener(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSupplierEvent(SupplierEvent event) {
        Supplier supplier = event.getSupplier();
        if (supplier != null && supplier.getId() != null) {
            ActivityLog log = new ActivityLog();

            switch (event.getActionType()) {
                case CREATED:
                    log.setType("SUPPLIER_CREATED");
                    log.setTitle("Nuevo Proveedor Creado");
                    log.setDescription("Proveedor #" + supplier.getId() + " con el nombre de  " + supplier.getName());
                    break;
                case UPDATED:
                    log.setType("SUPPLIER_UPDATED");
                    log.setTitle("Proveedor Actualizado");
                    log.setDescription("Se actualizó el proveedor #" + supplier.getId() + " con el nuevo nombre de  " + supplier.getName());
                    break;
                case DELETED:
                    log.setType("SUPPLIER_DELETED");
                    log.setTitle("Proveedor Eliminado");
                    log.setDescription("Se eliminó el proveedor #" + supplier.getId() + " con el nombre de  " + supplier.getName());
                    break;
            }

            activityLogService.saveActivityLog(log);
        }
    }
}
