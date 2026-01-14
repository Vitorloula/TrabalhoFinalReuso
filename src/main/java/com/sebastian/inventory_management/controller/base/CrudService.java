package com.sebastian.inventory_management.controller.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<DTO, RequestDTO, ID> {

    DTO getById(ID id);

    List<DTO> getAll();

    Page<DTO> getAllPaginated(Pageable pageable);

    DTO save(RequestDTO request);

    DTO update(ID id, RequestDTO request);

    void delete(ID id);
}
