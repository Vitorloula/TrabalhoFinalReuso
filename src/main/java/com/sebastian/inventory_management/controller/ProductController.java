package com.sebastian.inventory_management.controller;

import com.sebastian.inventory_management.DTO.Product.ProductRequestDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.controller.base.AbstractCrudController;
import com.sebastian.inventory_management.controller.base.CrudService;
import com.sebastian.inventory_management.controller.util.ResponseBuilder;
import com.sebastian.inventory_management.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController extends AbstractCrudController<
        ProductResponseDTO,
        ProductRequestDTO,
        Long> {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @Override
    protected CrudService<ProductResponseDTO, ProductRequestDTO, Long> getService() {
        return new CrudService<ProductResponseDTO, ProductRequestDTO, Long>() {
            @Override
            public ProductResponseDTO getById(Long id) {
                return productService.getProductById(id);
            }

            @Override
            public List<ProductResponseDTO> getAll() {
                return productService.getAllProducts();
            }

            @Override
            public Page<ProductResponseDTO> getAllPaginated(Pageable pageable) {
                return productService.getAllProductsPaginated(pageable);
            }

            @Override
            public ProductResponseDTO save(ProductRequestDTO request) {
                return productService.saveProduct(request);
            }

            @Override
            public ProductResponseDTO update(Long id, ProductRequestDTO request) {
                return productService.updateProduct(id, request);
            }

            @Override
            public void delete(Long id) {
                productService.deleteProduct(id);
            }
        };
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponseDTO> getProductByName(@PathVariable String name) {
        ProductResponseDTO product = productService.getProductByName(name);
        return ResponseBuilder.ok(product);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/category/{categoryId}/all")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        Page<ProductResponseDTO> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySupplierId(@PathVariable Long supplierId) {
        List<ProductResponseDTO> products = productService.getProductBySupplierId(supplierId);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDTO>> searchProductsByName(
            @RequestParam String name,
            Pageable pageable) {
        Page<ProductResponseDTO> products = productService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/stock/less/{stockThreshold}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStockLessThan(@PathVariable int stockThreshold) {
        List<ProductResponseDTO> products = productService.findByStockLessThan(stockThreshold);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/stock/more/{stockThreshold}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStockMoreThan(@PathVariable int stockThreshold) {
        List<ProductResponseDTO> products = productService.findByStockMoreThan(stockThreshold);
        return ResponseBuilder.ok(products);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/total-inventory")
    public ResponseEntity<Integer> getTotalInventory() {
        Integer totalInventory = productService.getTotalInventory();
        return ResponseBuilder.ok(totalInventory);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/inventory-by-category")
    public ResponseEntity<List<ProductResponseDTO>> getInventoryByCategory() {
        List<ProductResponseDTO> inventoryByCategory = productService.getInventoryByCategory();
        return ResponseBuilder.ok(inventoryByCategory);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/products-by-category")
    public ResponseEntity<List<ProductResponseDTO>> countProductsByCategory() {
        List<ProductResponseDTO> productsByCategory = productService.countProductsByCategory();
        return ResponseBuilder.ok(productsByCategory);
    }
}
