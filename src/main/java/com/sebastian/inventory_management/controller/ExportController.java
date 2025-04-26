package com.sebastian.inventory_management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.service.ICategoryService;
import com.sebastian.inventory_management.service.IExportService;
import com.sebastian.inventory_management.service.IOrderService;
import com.sebastian.inventory_management.service.IProductService;
import com.sebastian.inventory_management.service.ISupplierService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private IExportService exportService;
    private IProductService productService;
    private ISupplierService supplierService;
    private ICategoryService categoryService;
    private IOrderService orderService;

    @Autowired
    public ExportController(IExportService exportService, IProductService productService, ISupplierService supplierService, ICategoryService categoryService, IOrderService orderService) {
        this.orderService = orderService;
        this.categoryService = categoryService;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/categories")
    public void exportCategoriesToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=categorias.xlsx");

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        var excelStream = exportService.exportCategoriesToExcel(categories);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/orders")
    public void exportOrdersToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ordenes.xlsx");

        List<OrderResponseDTO> orders = orderService.getAllOrders();
        var excelStream = exportService.exportOrdersToExcel(orders);

        excelStream.transferTo(response.getOutputStream());
    }
}
