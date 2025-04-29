package com.sebastian.inventory_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sebastian.inventory_management.DTO.Category.CategoryRequestDTO;
import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.model.Category;

public interface ICategoryService {
    CategoryResponseDTO saveCategory(CategoryRequestDTO category);
    CategoryResponseDTO getCategoryById(Long id);
    Category getCategoryByIdEntity(Long id);

    List<CategoryResponseDTO> getAllCategories();
    Page<CategoryResponseDTO> getAllPageableCategories(Pageable pageable);
 
    boolean existsById(Long id);

    CategoryResponseDTO getCategoryByName(String name);
    Page<CategoryResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category);
    void deleteCategory(Long id);
}
