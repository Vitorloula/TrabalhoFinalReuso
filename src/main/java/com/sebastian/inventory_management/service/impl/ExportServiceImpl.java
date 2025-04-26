package com.sebastian.inventory_management.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.OrderItem.OrderItemResponseDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.service.IExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ExportServiceImpl implements IExportService {

    @Override
    public ByteArrayInputStream exportProductsToExcel(List<ProductResponseDTO> products) throws IOException {
        String[] COLUMNs = { "ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría", "Categoria-ID", "Proveedor",
                "Proveedor-ID" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (ProductResponseDTO product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice().doubleValue());
                row.createCell(4).setCellValue(product.getStock());
                row.createCell(5).setCellValue(product.getCategoryName());
                row.createCell(6).setCellValue(product.getCategoryId());
                row.createCell(7).setCellValue(product.getSupplierName());
                row.createCell(8).setCellValue(product.getSupplierId());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportSuppliersToExcel(List<SupplierResponseDTO> suppliers) throws IOException {
        String[] COLUMNs = { "ID", "Nombre", "Email de Contacto", "Numero de Telefono" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Proveedores");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (SupplierResponseDTO supplier : suppliers) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(supplier.getId());
                row.createCell(1).setCellValue(supplier.getName());
                row.createCell(2).setCellValue(supplier.getContactEmail());
                row.createCell(3).setCellValue(supplier.getPhoneNumber());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        }
    }

    @Override
    public ByteArrayInputStream exportCategoriesToExcel(List<CategoryResponseDTO> categories) throws IOException {
        String[] COLUMNs = { "ID", "Nombre" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Categorias");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (CategoryResponseDTO category : categories) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(category.getId());
                row.createCell(1).setCellValue(category.getName());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public ByteArrayInputStream exportOrdersToExcel(List<OrderResponseDTO> orders) throws IOException {
        String[] COLUMNs = {
                "ID Orden", "Número de Orden", "Fecha", "Proveedor", "Proveedor-ID", "Total Orden",
                "Producto", "Cantidad", "Precio Unitario"
        };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Órdenes");

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;

            for (OrderResponseDTO order : orders) {
                List<OrderItemResponseDTO> items = order.getItems();

                if (items.isEmpty()) {
                    Row row = sheet.createRow(rowIdx++);
                    fillOrderCells(row, order);
                } else {
                    for (OrderItemResponseDTO item : items) {
                        Row row = sheet.createRow(rowIdx++);
                        fillOrderCells(row, order);
                        row.createCell(6).setCellValue(item.getProductName());
                        row.createCell(7).setCellValue(item.getQuantity());
                        row.createCell(8).setCellValue(item.getPrice().doubleValue());
                    }
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void fillOrderCells(Row row, OrderResponseDTO order) {
        row.createCell(0).setCellValue(order.getId());
        row.createCell(1).setCellValue(order.getOrderNumber());
        row.createCell(2).setCellValue(order.getOrderDate().toString());
        row.createCell(3).setCellValue(order.getSupplierName());
        row.createCell(4).setCellValue(order.getSupplierId());
        row.createCell(5).setCellValue(calculateOrderTotal(order.getItems()).doubleValue());
    }

   

    private BigDecimal calculateOrderTotal(List<OrderItemResponseDTO> items) {
        return items.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice(); 
                    Integer quantity = item.getQuantity();
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    

}
