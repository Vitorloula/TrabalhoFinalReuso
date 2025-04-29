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
import com.sebastian.inventory_management.exception.ResourceNotFoundException;
import com.sebastian.inventory_management.mapper.ProductMapper;
import com.sebastian.inventory_management.model.Category;
import com.sebastian.inventory_management.model.Product;
import com.sebastian.inventory_management.model.Supplier;
import com.sebastian.inventory_management.repository.ProductRepository;
import com.sebastian.inventory_management.service.ICategoryService;
import com.sebastian.inventory_management.service.IProductService;
import com.sebastian.inventory_management.service.ISupplierService;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final ISupplierService supplierService;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ICategoryService categoryService,
            ISupplierService supplierService, ProductMapper productMapper, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO product) {
        validateUniqueProductName(product.getName(), null);
        Category category = categoryService.getCategoryByIdEntity(product.getCategoryId());
        Supplier supplier = supplierService.getSupplierByIdEntity(product.getSupplierId());

        Product productToSave = productMapper.toEntity(product);
        productToSave.setCategory(category);
        productToSave.setSupplier(supplier);

        Product savedProduct = productRepository.save(productToSave);

        eventPublisher.publishEvent(new ProductEvent(savedProduct, ActionType.CREATED));
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + name));
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProductsPaginated(Pageable pageable) {
       Page <Product> products = productRepository.findAll(pageable);
       return productMapper.toDTOPage(products);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryService.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        return productMapper.toDTOPage(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        if (!categoryService.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        List<Product> products = productRepository.findByCategoryId(categoryId);
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductBySupplierId(Long supplierId) {
        if (!supplierService.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + supplierId);
        }

        List<Product> products = productRepository.findBySupplierId(supplierId);
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        return productMapper.toDTOPage(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByStockLessThan(int stockThreshold) {
        List<Product> products = productRepository.findByStockLessThan(stockThreshold);
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByStockMoreThan(int stockThreshold) {
        List<Product> products = productRepository.findByStockMoreThan(stockThreshold);
        return productMapper.toDTOList(products);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalInventory() {
        return productRepository.getTotalInventory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getInventoryByCategory() {
        return productRepository.getInventoryByCategory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> countProductsByCategory() {
        return productRepository.countProductsByCategory();
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO product) {
        Product productToUpdate = getProductByIdEntity(id);
        validateUniqueProductName(product.getName(), id);
        Category category = categoryService.getCategoryByIdEntity(product.getCategoryId());
        Supplier supplier = supplierService.getSupplierByIdEntity(product.getSupplierId());

        productMapper.updateEntityFromDto(product, productToUpdate);
        productToUpdate.setCategory(category);
        productToUpdate.setSupplier(supplier);

        Product updatedProduct = productRepository.save(productToUpdate);
        eventPublisher.publishEvent(new ProductEvent(updatedProduct, ActionType.UPDATED));
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductByIdEntity(id);
        productRepository.delete(product);
        eventPublisher.publishEvent(new ProductEvent(product, ActionType.DELETED));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductByIdEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void validateUniqueProductName(String name, Long excludeId) {
        productRepository.findByName(name).ifPresent(existing -> {
            if (excludeId == null || !existing.getId().equals(excludeId)) {
                throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
            }
        });
    }

   

   
}
