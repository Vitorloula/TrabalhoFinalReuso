package com.sebastian.inventory_management.controller.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sebastian.inventory_management.controller.util.ResponseBuilder;

import java.util.List;

import jakarta.validation.Valid;

public abstract class AbstractCrudController<DTO, RequestDTO, ID> {

    protected abstract CrudService<DTO, RequestDTO, ID> getService();

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable ID id) {
        DTO dto = getService().getById(id);
        return ResponseBuilder.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<DTO>> getAll() {
        List<DTO> dtos = getService().getAll();
        return ResponseBuilder.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/paginated")
    public ResponseEntity<Page<DTO>> getAllPaginated(Pageable pageable) {
        Page<DTO> page = getService().getAllPaginated(pageable);
        return ResponseBuilder.ok(page);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<DTO> create(@Valid @RequestBody RequestDTO request) {
        DTO created = getService().save(request);
        return ResponseBuilder.created(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable ID id, @Valid @RequestBody RequestDTO request) {
        DTO updated = getService().update(id, request);
        return ResponseBuilder.ok(updated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseBuilder.noContent();
    }
}
