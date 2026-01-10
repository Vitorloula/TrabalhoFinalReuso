package com.sebastian.inventory_management.event.base;

import com.sebastian.inventory_management.model.ActivityLog;
import com.sebastian.inventory_management.service.IActivityLogService;

/**
 * Template Method para tratamento de eventos de domínio.
 * Define o algoritmo padrão para criação de ActivityLog, permitindo
 * que subclasses customizem apenas os títulos e descrições.
 * 
 * Padrão GoF: Template Method
 * 
 * @param <E> Tipo do evento que estende BaseEvent
 */
public abstract class AbstractEventListener<E extends BaseEvent<?>> {

    protected final IActivityLogService activityLogService;

    protected AbstractEventListener(IActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    /**
     * Template Method - define o algoritmo padrão para handling de eventos.
     * Este método não deve ser sobrescrito pelas subclasses.
     */
    protected void handleEvent(E event) {
        if (event.getEntityId() == null) {
            return;
        }

        ActivityLog log = new ActivityLog();
        String prefix = event.getEntityTypePrefix();

        switch (event.getActionType()) {
            case CREATED:
                log.setType(prefix + "_CREATED");
                log.setTitle(getTitleCreated());
                log.setDescription(getDescriptionCreated(event));
                break;
            case UPDATED:
                log.setType(prefix + "_UPDATED");
                log.setTitle(getTitleUpdated());
                log.setDescription(getDescriptionUpdated(event));
                break;
            case DELETED:
                log.setType(prefix + "_DELETED");
                log.setTitle(getTitleDeleted());
                log.setDescription(getDescriptionDeleted(event));
                break;
        }

        activityLogService.saveActivityLog(log);
    }

    // ==================== Métodos abstratos para customização (hooks)
    // ====================

    /**
     * Retorna o título para eventos de criação.
     */
    protected abstract String getTitleCreated();

    /**
     * Retorna o título para eventos de atualização.
     */
    protected abstract String getTitleUpdated();

    /**
     * Retorna o título para eventos de exclusão.
     */
    protected abstract String getTitleDeleted();

    // ==================== Métodos com implementação padrão (podem ser
    // sobrescritos) ====================

    /**
     * Retorna a descrição para eventos de criação.
     * Implementação padrão usa getEntityDescription() do evento.
     */
    protected String getDescriptionCreated(E event) {
        return event.getEntityDescription();
    }

    /**
     * Retorna a descrição para eventos de atualização.
     * Implementação padrão usa getEntityDescription() do evento.
     */
    protected String getDescriptionUpdated(E event) {
        return "Se actualizó " + event.getEntityDescription();
    }

    /**
     * Retorna a descrição para eventos de exclusão.
     * Implementação padrão usa getEntityDescription() do evento.
     */
    protected String getDescriptionDeleted(E event) {
        return "Se eliminó " + event.getEntityDescription();
    }
}
