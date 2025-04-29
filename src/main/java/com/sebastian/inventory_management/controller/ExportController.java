package com.sebastian.inventory_management.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.DTO.User.UserResponseDTO;
import com.sebastian.inventory_management.service.ICategoryService;
import com.sebastian.inventory_management.service.IExportExcelService;
import com.sebastian.inventory_management.service.IExportPDFService;
import com.sebastian.inventory_management.service.IOrderService;
import com.sebastian.inventory_management.service.IProductService;
import com.sebastian.inventory_management.service.ISupplierService;
import com.sebastian.inventory_management.service.IUserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private IExportExcelService exportService;
    private IExportPDFService exportPDFService;
    private IProductService productService;
    private ISupplierService supplierService;
    private ICategoryService categoryService;
    private IOrderService orderService;
    private IUserService userService;

    @Autowired
    public ExportController(IExportExcelService exportService, IExportPDFService exportPDFService,
            IProductService productService, ISupplierService supplierService, ICategoryService categoryService,
            IOrderService orderService, IUserService userService) {
        this.orderService = orderService;
        this.exportPDFService = exportPDFService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.exportService = exportService;
        this.productService = productService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/products")
    public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos.xlsx");

        List<ProductResponseDTO> products = productService.getAllProducts();
        var excelStream = exportService.exportProductsToExcel(products);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/suppliers")
    public void exportSuppliersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=productos.xlsx");

        List<SupplierResponseDTO> suppliers = supplierService.getAllSuppliers();
        var excelStream = exportService.exportSuppliersToExcel(suppliers);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/categories")
    public void exportCategoriesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=categorias.xlsx");

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        var excelStream = exportService.exportCategoriesToExcel(categories);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/orders")
    public void exportOrdersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ordenes.xlsx");

        List<OrderResponseDTO> orders = orderService.getAllOrders();
        var excelStream = exportService.exportOrdersToExcel(orders);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/users")
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        List<UserResponseDTO> users = userService.getAllUsers();
        var excelStream = exportService.exportUsersToExcel(users);

        excelStream.transferTo(response.getOutputStream());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/orders/{id}/export-pdf")
    public ResponseEntity<InputStreamResource> exportOrderToPDF(@PathVariable Long id) throws IOException {
        OrderResponseDTO order = orderService.getOrderById(id);
        ByteArrayInputStream bis = exportPDFService.exportOrderDetailsToPDF(order);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=orden_" + order.getOrderNumber() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
