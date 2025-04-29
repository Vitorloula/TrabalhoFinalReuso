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
import com.sebastian.inventory_management.exception.ResourceNotFoundException;
import com.sebastian.inventory_management.mapper.SupplierMapper;
import com.sebastian.inventory_management.model.Supplier;
import com.sebastian.inventory_management.repository.SupplierRepository;
import com.sebastian.inventory_management.service.ISupplierService;

@Service
public class SupplierServiceImpl implements ISupplierService{

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierMapper supplierMapper, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    @Override
    @Transactional
    public SupplierResponseDTO saveSupplier(SupplierRequestDTO supplier) {
        validateUniqueSupplier(supplier.getName(), supplier.getContactEmail(), null);
        Supplier supplierToSave = supplierMapper.toEntity(supplier);
        Supplier savedSupplier = supplierRepository.save(supplierToSave);
        eventPublisher.publishEvent(new SupplierEvent(savedSupplier, ActionType.CREATED));
        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page <SupplierResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findByNameContainingIgnoreCase(name, pageable);
        return supplierMapper.toDTOPage(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierByExactName(String name) {
        Supplier supplier = supplierRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with name: " + name));
        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierByContactEmail(String contactEmail) {
        Supplier supplier = supplierRepository.findByContactEmail(contactEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with contact email: " + contactEmail));
        return supplierMapper.toDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return supplierMapper.toDTOList(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponseDTO> getAllSuppliersPaginated(Pageable pageable) {
        Page<Supplier> suppliers = supplierRepository.findAll(pageable);
        return supplierMapper.toDTOPage(suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countActiveSuppliers() {
        return supplierRepository.countActiveSuppliers();
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierByIdEntity(id);
        supplierRepository.delete(supplier);
        eventPublisher.publishEvent(new SupplierEvent(supplier, ActionType.DELETED));
    }

    @Override
    @Transactional
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO supplier) {
        Supplier supplierToUpdate = getSupplierByIdEntity(id);
        validateUniqueSupplier(supplier.getName(), supplier.getContactEmail(), id);
        supplierMapper.updateEntityFromDto(supplier, supplierToUpdate);
        supplierRepository.save(supplierToUpdate);
        eventPublisher.publishEvent(new SupplierEvent(supplierToUpdate, ActionType.UPDATED));
        return supplierMapper.toDTO(supplierToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return supplierRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier getSupplierByIdEntity(Long id) {
        return supplierRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }

    private void validateUniqueSupplier(String name, String contactEmail, Long excludeId) {
        supplierRepository.findByName(name).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Supplier with name '" + name + "' already exists.");
            }
        });
    
        supplierRepository.findByContactEmail(contactEmail).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Supplier with contact email '" + contactEmail + "' already exists.");
            }
        });
    }

   

  

    
}
