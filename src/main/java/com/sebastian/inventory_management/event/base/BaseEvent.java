package com.sebastian.inventory_management.event.base;

import com.sebastian.inventory_management.enums.ActionType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Classe base genérica para eventos de domínio.
 * Elimina duplicação de código entre ProductEvent, CategoryEvent, SupplierEvent
 * e OrderEvent.
 * 
 * @param <T> Tipo da entidade encapsulada pelo evento
 */
@AllArgsConstructor
@Data
public abstract class BaseEvent<T> {

    private final T entity;
    private final ActionType actionType;

    /**
     * Retorna o identificador único da entidade.
     */
    public abstract Long getEntityId();

    /**
     * Retorna o nome/descrição da entidade para logs.
     */
    public abstract String getEntityDescription();

    /**
     * Retorna o prefixo do tipo para ActivityLog (ex: "PRODUCT", "ORDER").
     */
    public abstract String getEntityTypePrefix();
}
