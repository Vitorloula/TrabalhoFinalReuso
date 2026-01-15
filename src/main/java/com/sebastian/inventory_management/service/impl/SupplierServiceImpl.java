package com.sebastian.inventory_management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebastian.inventory_management.DTO.Supplier.SupplierRequestDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.Supplier.SupplierEvent;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.mapper.PageMapperUtil;
import com.sebastian.inventory_management.mapper.SupplierMapper;
import com.sebastian.inventory_management.model.Supplier;
import com.sebastian.inventory_management.repository.SupplierRepository;
import com.sebastian.inventory_management.service.ISupplierService;
import com.sebastian.inventory_management.service.base.AbstractCrudService;

@Service
public class SupplierServiceImpl extends
        AbstractCrudService<Supplier, SupplierResponseDTO, SupplierRequestDTO, Long, SupplierRepository, SupplierMapper>
        implements ISupplierService {

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper,
            ApplicationEventPublisher eventPublisher) {
        super(supplierRepository, supplierMapper, eventPublisher);
    }

    @Override
    protected String getEntityName() {
        return "Supplier";
    }

    @Override
    protected SupplierResponseDTO toDTO(Supplier entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected List<SupplierResponseDTO> toDTOList(List<Supplier> entities) {
        return mapper.toDTOList(entities);
    }

    @Override
    protected Supplier toEntity(SupplierRequestDTO request) {
        return mapper.toEntity(request);
    }

    @Override
    protected void updateEntityFromDto(SupplierRequestDTO request, Supplier entity) {
        mapper.updateEntityFromDto(request, entity);
    }

    @Override
    protected BaseEvent<?> createEvent(Supplier entity, ActionType actionType) {
        return new SupplierEvent(entity, actionType);
    }

    @Override
    protected void validateBeforeSave(SupplierRequestDTO request, Long excludeId) {
        validateUniqueSupplier(request.getName(), request.getContactEmail(), excludeId);
    }

    @Override
    public SupplierResponseDTO saveSupplier(SupplierRequestDTO supplier) {
        return save(supplier);
    }

    @Override
    public SupplierResponseDTO getSupplierById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier getSupplierByIdEntity(Long id) {
        return getByIdEntity(id);
    }

    @Override
    public List<SupplierResponseDTO> getAllSuppliers() {
        return getAll();
    }

    @Override
    public Page<SupplierResponseDTO> getAllSuppliersPaginated(Pageable pageable) {
        return getAllPaginated(pageable);
    }

    @Override
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO supplier) {
        return update(id, supplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        delete(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Supplier> suppliers = repository.findByNameContainingIgnoreCase(name, pageable);
        return PageMapperUtil.toPage(suppliers, mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierByExactName(String name) {
        Supplier supplier = repository.findByName(name)
                .orElseThrow(() -> new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                        "Supplier not found with name: " + name));
        return mapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierByContactEmail(String contactEmail) {
        Supplier supplier = repository.findByContactEmail(contactEmail)
                .orElseThrow(() -> new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                        "Supplier not found with contact email: " + contactEmail));
        return mapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveSuppliers() {
        return repository.countActiveSuppliers();
    }

    private void validateUniqueSupplier(String name, String contactEmail, Long excludeId) {
        com.sebastian.inventory_management.util.ValidationHelper.validateUniqueName(
                repository.findByName(name), excludeId, Supplier::getId, "Supplier", name);

        com.sebastian.inventory_management.util.ValidationHelper.validateUniqueField(
                repository.findByContactEmail(contactEmail), excludeId, Supplier::getId,
                "Supplier", "contact email", contactEmail);
    }
}
