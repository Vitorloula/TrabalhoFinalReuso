package com.sebastian.inventory_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.controller.base.AbstractCrudController;
import com.sebastian.inventory_management.controller.base.CrudService;
import com.sebastian.inventory_management.controller.util.ResponseBuilder;
import com.sebastian.inventory_management.DTO.Supplier.SupplierRequestDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.service.ISupplierService;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController extends AbstractCrudController<
        SupplierResponseDTO,
        SupplierRequestDTO,
        Long> {

    private final ISupplierService supplierService;

    @Autowired
    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    protected CrudService<SupplierResponseDTO, SupplierRequestDTO, Long> getService() {
        return new CrudService<SupplierResponseDTO, SupplierRequestDTO, Long>() {
            @Override
            public SupplierResponseDTO getById(Long id) {
                return supplierService.getSupplierById(id);
            }

            @Override
            public List<SupplierResponseDTO> getAll() {
                return supplierService.getAllSuppliers();
            }

            @Override
            public Page<SupplierResponseDTO> getAllPaginated(Pageable pageable) {
                return supplierService.getAllSuppliersPaginated(pageable);
            }

            @Override
            public SupplierResponseDTO save(SupplierRequestDTO request) {
                return supplierService.saveSupplier(request);
            }

            @Override
            public SupplierResponseDTO update(Long id, SupplierRequestDTO request) {
                return supplierService.updateSupplier(id, request);
            }

            @Override
            public void delete(Long id) {
                supplierService.deleteSupplier(id);
            }
        };
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/search")
    public ResponseEntity<Page<SupplierResponseDTO>> searchSuppliersByName(
            @RequestParam String name,
            Pageable pageable) {
        Page<SupplierResponseDTO> suppliers = supplierService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseBuilder.ok(suppliers);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/contact-email/{contactEmail}")
    public ResponseEntity<SupplierResponseDTO> getSupplierByContactEmail(@PathVariable String contactEmail) {
        SupplierResponseDTO supplier = supplierService.getSupplierByContactEmail(contactEmail);
        return ResponseBuilder.ok(supplier);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/count")
    public ResponseEntity<Long> getCountSuppliers() {
        Long count = supplierService.countActiveSuppliers();
        return ResponseBuilder.ok(count);
    }
}
