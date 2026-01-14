package com.sebastian.inventory_management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sebastian.inventory_management.DTO.Product.ProductRequestDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.enums.ActionType;
import com.sebastian.inventory_management.event.Product.ProductEvent;
import com.sebastian.inventory_management.event.base.BaseEvent;
import com.sebastian.inventory_management.mapper.PageMapperUtil;
import com.sebastian.inventory_management.mapper.ProductMapper;
import com.sebastian.inventory_management.model.Category;
import com.sebastian.inventory_management.model.Product;
import com.sebastian.inventory_management.model.Supplier;
import com.sebastian.inventory_management.repository.ProductRepository;
import com.sebastian.inventory_management.service.ICategoryService;
import com.sebastian.inventory_management.service.IProductService;
import com.sebastian.inventory_management.service.ISupplierService;
import com.sebastian.inventory_management.service.base.AbstractCrudService;

@Service
public class ProductServiceImpl extends AbstractCrudService<
        Product,
        ProductResponseDTO,
        ProductRequestDTO,
        Long,
        ProductRepository,
        ProductMapper> implements IProductService {

    private final ICategoryService categoryService;
    private final ISupplierService supplierService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ICategoryService categoryService,
            ISupplierService supplierService, ProductMapper productMapper, ApplicationEventPublisher eventPublisher) {
        super(productRepository, productMapper, eventPublisher);
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

    @Override
    protected ProductResponseDTO toDTO(Product entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected List<ProductResponseDTO> toDTOList(List<Product> entities) {
        return mapper.toDTOList(entities);
    }

    @Override
    protected Product toEntity(ProductRequestDTO request) {
        Product product = mapper.toEntity(request);
        Category category = categoryService.getCategoryByIdEntity(request.getCategoryId());
        Supplier supplier = supplierService.getSupplierByIdEntity(request.getSupplierId());
        product.setCategory(category);
        product.setSupplier(supplier);
        return product;
    }

    @Override
    protected void updateEntityFromDto(ProductRequestDTO request, Product entity) {
        mapper.updateEntityFromDto(request, entity);
        Category category = categoryService.getCategoryByIdEntity(request.getCategoryId());
        Supplier supplier = supplierService.getSupplierByIdEntity(request.getSupplierId());
        entity.setCategory(category);
        entity.setSupplier(supplier);
    }

    @Override
    protected BaseEvent<?> createEvent(Product entity, ActionType actionType) {
        return new ProductEvent(entity, actionType);
    }

    @Override
    protected void validateBeforeSave(ProductRequestDTO request, Long excludeId) {
        validateUniqueProductName(request.getName(), excludeId);
    }

    @Override
    public ProductResponseDTO saveProduct(ProductRequestDTO product) {
        return save(product);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        return getById(id);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return getAll();
    }

    @Override
    public Page<ProductResponseDTO> getAllProductsPaginated(Pageable pageable) {
        return getAllPaginated(pageable);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO product) {
        return update(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductByName(String name) {
        Product product = repository.findByName(name)
                .orElseThrow(() -> new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                        "Product not found with name: " + name));
        return mapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryService.existsById(categoryId)) {
            throw new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                    "Category not found with id: " + categoryId);
        }
        Page<Product> products = repository.findByCategoryId(categoryId, pageable);
        return PageMapperUtil.toPage(products, mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            throw new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                    "Category not found with id: " + categoryId);
        }
        List<Product> products = repository.findByCategoryId(categoryId);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductBySupplierId(Long supplierId) {
        if (!supplierService.existsById(supplierId)) {
            throw new com.sebastian.inventory_management.exception.ResourceNotFoundException(
                    "Supplier not found with id: " + supplierId);
        }
        List<Product> products = repository.findBySupplierId(supplierId);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Product> products = repository.findByNameContainingIgnoreCase(name, pageable);
        return PageMapperUtil.toPage(products, mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByStockLessThan(int stockThreshold) {
        List<Product> products = repository.findByStockLessThan(stockThreshold);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByStockMoreThan(int stockThreshold) {
        List<Product> products = repository.findByStockMoreThan(stockThreshold);
        return mapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalInventory() {
        return repository.getTotalInventory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getInventoryByCategory() {
        return repository.getInventoryByCategory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> countProductsByCategory() {
        return repository.countProductsByCategory();
    }

    private void validateUniqueProductName(String name, Long excludeId) {
        repository.findByName(name).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
            }
        });
    }
}
