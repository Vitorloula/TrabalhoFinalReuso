package com.sebastian.inventory_management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.service.IExportService;
import com.sebastian.inventory_management.service.IProductService;
import com.sebastian.inventory_management.service.ISupplierService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private IExportService exportService;
    private IProductService productService;
    private ISupplierService supplierService;

    @Autowired
    public ExportController(IExportService exportService, IProductService productService, ISupplierService supplierService) {
        this.supplierService = supplierService;
        this.exportService = exportService;
        this.productService = productService;
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/products")
    public void exportProductsToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos.xlsx");

        List<ProductResponseDTO> products = productService.getAllProducts();
        var excelStream = exportService.exportProductsToExcel(products);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/suppliers")
    public void exportSuppliersToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos.xlsx");

        List<SupplierResponseDTO> suppliers = supplierService.getAllSuppliers();
        var excelStream = exportService.exportSuppliersToExcel(suppliers);

        excelStream.transferTo(response.getOutputStream());
    }
}
