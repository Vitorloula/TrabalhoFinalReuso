package com.sebastian.inventory_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    List <Product> findByCategoryId(Long categoryId);

    List<Product> findBySupplierId(Long supplierId);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.stock < :stockThreshold")
    List<Product> findByStockLessThan(@Param("stockThreshold") int stockThreshold);

    @Query("SELECT p FROM Product p WHERE p.stock > :stockThreshold")
    List<Product> findByStockMoreThan(@Param("stockThreshold") int stockThreshold);

    @Query("SELECT COUNT(p) FROM Product p")
    Integer getTotalInventory();

    @Query("SELECT new com.sebastian.inventory_management.DTO.Product.ProductResponseDTO(p.category.name, CAST(SUM(p.stock) AS integer)) FROM Product p GROUP BY p.category.name")
    List<ProductResponseDTO> getInventoryByCategory();

    @Query("SELECT new com.sebastian.inventory_management.DTO.Product.ProductResponseDTO(p.category.name, CAST(COUNT(p) AS integer)) FROM Product p GROUP BY p.category.name")
    List<ProductResponseDTO> countProductsByCategory();
}
