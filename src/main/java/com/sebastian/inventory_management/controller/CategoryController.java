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
import com.sebastian.inventory_management.DTO.Category.CategoryRequestDTO;
import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.service.ICategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController extends AbstractCrudController<
        CategoryResponseDTO,
        CategoryRequestDTO,
        Long> {

    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected CrudService<CategoryResponseDTO, CategoryRequestDTO, Long> getService() {
        return new CrudService<CategoryResponseDTO, CategoryRequestDTO, Long>() {
            @Override
            public CategoryResponseDTO getById(Long id) {
                return categoryService.getCategoryById(id);
            }

            @Override
            public List<CategoryResponseDTO> getAll() {
                return categoryService.getAllCategories();
            }

            @Override
            public Page<CategoryResponseDTO> getAllPaginated(Pageable pageable) {
                return categoryService.getAllPageableCategories(pageable);
            }

            @Override
            public CategoryResponseDTO save(CategoryRequestDTO request) {
                return categoryService.saveCategory(request);
            }

            @Override
            public CategoryResponseDTO update(Long id, CategoryRequestDTO request) {
                return categoryService.updateCategory(id, request);
            }

            @Override
            public void delete(Long id) {
                categoryService.deleteCategory(id);
            }
        };
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(@PathVariable String name) {
        CategoryResponseDTO category = categoryService.getCategoryByName(name);
        return ResponseBuilder.ok(category);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponseDTO>> searchCategoriesByName(
            @RequestParam String name,
            Pageable pageable) {
        Page<CategoryResponseDTO> categories = categoryService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseBuilder.ok(categories);
    }
}
