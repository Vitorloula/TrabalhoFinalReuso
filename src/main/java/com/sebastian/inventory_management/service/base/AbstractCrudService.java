package com.sebastian.inventory_management.service.base;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.exception.ResourceNotFoundException;
import com.sebastian.inventory_management.mapper.PageMapperUtil;

import java.util.List;

public abstract class AbstractCrudService<
        Entity,
        DTO,
        RequestDTO,
        ID,
        Repository extends JpaRepository<Entity, ID>,
        Mapper> {

    protected final Repository repository;
    protected final Mapper mapper;
    protected final ApplicationEventPublisher eventPublisher;

    protected AbstractCrudService(
            Repository repository,
            Mapper mapper,
            ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public DTO getById(ID id) {
        Entity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getEntityName() + " not found with id: " + id));
        return toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<DTO> getAll() {
        List<Entity> entities = repository.findAll();
        return toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<DTO> getAllPaginated(Pageable pageable) {
        Page<Entity> page = repository.findAll(pageable);
        return PageMapperUtil.toPage(page, this::toDTO);
    }

    @Transactional
    public DTO save(RequestDTO request) {
        validateBeforeSave(request, null);
        Entity entity = toEntity(request);
        Entity saved = repository.save(entity);
        publishEvent(saved, ActionType.CREATED);
        return toDTO(saved);
    }

    @Transactional
    public DTO update(ID id, RequestDTO request) {
        Entity entity = getByIdEntity(id);
        validateBeforeSave(request, id);
        updateEntityFromDto(request, entity);
        Entity updated = repository.save(entity);
        publishEvent(updated, ActionType.UPDATED);
        return toDTO(updated);
    }

    @Transactional
    public void delete(ID id) {
        Entity entity = getByIdEntity(id);
        repository.delete(entity);
        publishEvent(entity, ActionType.DELETED);
    }

    @Transactional(readOnly = true)
    public Entity getByIdEntity(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getEntityName() + " not found with id: " + id));
    }

    protected abstract String getEntityName();
    protected abstract DTO toDTO(Entity entity);
    protected abstract List<DTO> toDTOList(List<Entity> entities);
    protected abstract Entity toEntity(RequestDTO request);
    protected abstract void updateEntityFromDto(RequestDTO request, Entity entity);
    protected abstract BaseEvent<?> createEvent(Entity entity, ActionType actionType);

    protected void validateBeforeSave(RequestDTO request, ID excludeId) {}

    protected void publishEvent(Entity entity, ActionType actionType) {
        if (eventPublisher != null) {
            BaseEvent<?> event = createEvent(entity, actionType);
            if (event != null) {
                eventPublisher.publishEvent(event);
            }
        }
    }
}
