package com.sebastian.inventory_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sebastian.inventory_management.DTO.Product.ProductRequestDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.model.Product;

public interface IProductService {
    ProductResponseDTO saveProduct(ProductRequestDTO product);

    ProductResponseDTO getProductById(Long id);

    Product getProductByIdEntity(Long id);

    ProductResponseDTO getProductByName(String name);

    List<ProductResponseDTO> getAllProducts();

    Page<ProductResponseDTO> getAllProductsPaginated(Pageable pageable);

    Page<ProductResponseDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    List<ProductResponseDTO> getProductBySupplierId(Long supplierId);

    Page<ProductResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<ProductResponseDTO> findByStockLessThan(int stockThreshold);

    List<ProductResponseDTO> findByStockMoreThan(int stockThreshold);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO product);

    void deleteProduct(Long id);

    Integer getTotalInventory();

    List<ProductResponseDTO> getInventoryByCategory();

    List<ProductResponseDTO> countProductsByCategory();
}
