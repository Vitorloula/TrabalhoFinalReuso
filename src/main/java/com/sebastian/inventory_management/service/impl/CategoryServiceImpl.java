package com.sebastian.inventory_management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebastian.inventory_management.DTO.Category.CategoryRequestDTO;
import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.Category.CategoryEvent;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.mapper.CategoryMapper;
import com.sebastian.inventory_management.mapper.PageMapperUtil;
import com.sebastian.inventory_management.model.Category;
import com.sebastian.inventory_management.repository.CategoryRepository;
import com.sebastian.inventory_management.service.ICategoryService;
import com.sebastian.inventory_management.service.base.AbstractCrudService;

@Service
public class CategoryServiceImpl extends AbstractCrudService<
        Category,
        CategoryResponseDTO,
        CategoryRequestDTO,
        Long,
        CategoryRepository,
        CategoryMapper> implements ICategoryService {

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
            ApplicationEventPublisher eventPublisher) {
        super(categoryRepository, categoryMapper, eventPublisher);
    }

    @Override
    protected String getEntityName() {
        return "Category";
    }

    @Override
    protected CategoryResponseDTO toDTO(Category entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected List<CategoryResponseDTO> toDTOList(List<Category> entities) {
        return mapper.toDTOList(entities);
    }

    @Override
    protected Category toEntity(CategoryRequestDTO request) {
        return mapper.toEntity(request);
    }

    @Override
    protected void updateEntityFromDto(CategoryRequestDTO request, Category entity) {
        mapper.updateEntityFromDto(request, entity);
    }

    @Override
    protected BaseEvent<?> createEvent(Category entity, ActionType actionType) {
        return new CategoryEvent(entity, actionType);
    }

    @Override
    protected void validateBeforeSave(CategoryRequestDTO request, Long excludeId) {
        validateUniqueCategoryName(request.getName(), excludeId);
    }

    @Override
    public CategoryResponseDTO saveCategory(CategoryRequestDTO category) {
        return save(category);
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        return getById(id);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        return getAll();
    }

    @Override
    public Page<CategoryResponseDTO> getAllPageableCategories(Pageable pageable) {
        return getAllPaginated(pageable);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category) {
        return update(id, category);
    }

    @Override
    public void deleteCategory(Long id) {
        delete(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryByName(String name) {
        Category category = repository.findByName(name)
                .orElseThrow(() -> new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                        "Category not found with name: " + name));
        return mapper.toDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Category> categories = repository.findByNameContainingIgnoreCase(name, pageable);
        return PageMapperUtil.toPage(categories, mapper::toDTO);
    }

    private void validateUniqueCategoryName(String name, Long excludeId) {
        repository.findByName(name).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Category with name '" + name + "' already exists.");
            }
        });
    }
}
