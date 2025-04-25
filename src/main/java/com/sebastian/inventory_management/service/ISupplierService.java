package com.sebastian.inventory_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sebastian.inventory_management.DTO.Supplier.SupplierRequestDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.model.Supplier;

public interface ISupplierService {
    SupplierResponseDTO saveSupplier(SupplierRequestDTO supplier);
    SupplierResponseDTO getSupplierById(Long id);
    boolean existsById(Long id);
    Supplier getSupplierByIdEntity(Long id);
    Page <SupplierResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable);
    SupplierResponseDTO getSupplierByExactName(String name);
    SupplierResponseDTO getSupplierByContactEmail(String contactEmail);
    List<SupplierResponseDTO> getAllSuppliers();
    Page<SupplierResponseDTO> getAllSuppliersPaginated(Pageable pageable);
    Long countActiveSuppliers();
    void deleteSupplier(Long id);
    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO supplier);
}
