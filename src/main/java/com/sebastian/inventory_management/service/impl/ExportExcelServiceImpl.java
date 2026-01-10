package com.sebastian.inventory_management.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.OrderItem.OrderItemResponseDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.DTO.User.UserResponseDTO;
import com.sebastian.inventory_management.service.IExportExcelService;
import com.sebastian.inventory_management.service.export.CategoryExcelExporter;
import com.sebastian.inventory_management.service.export.ProductExcelExporter;
import com.sebastian.inventory_management.service.export.SupplierExcelExporter;
import com.sebastian.inventory_management.service.export.UserExcelExporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Implementação do serviço de exportação Excel.
 * Utiliza exportadores específicos baseados em Template Method.
 * 
 * Nota: Orders tem lógica especial (items aninhados) e mantém implementação
 * própria.
 */
@Service
public class ExportExcelServiceImpl implements IExportExcelService {

    private final ProductExcelExporter productExporter;
    private final SupplierExcelExporter supplierExporter;
    private final CategoryExcelExporter categoryExporter;
    private final UserExcelExporter userExporter;

    public ExportExcelServiceImpl(
            ProductExcelExporter productExporter,
            SupplierExcelExporter supplierExporter,
            CategoryExcelExporter categoryExporter,
            UserExcelExporter userExporter) {
        this.productExporter = productExporter;
        this.supplierExporter = supplierExporter;
        this.categoryExporter = categoryExporter;
        this.userExporter = userExporter;
    }

    @Override
    public ByteArrayInputStream exportProductsToExcel(List<ProductResponseDTO> products) throws IOException {
        return productExporter.export(products);
    }

    @Override
    public ByteArrayInputStream exportSuppliersToExcel(List<SupplierResponseDTO> suppliers) throws IOException {
        return supplierExporter.export(suppliers);
    }

    @Override
    public ByteArrayInputStream exportCategoriesToExcel(List<CategoryResponseDTO> categories) throws IOException {
        return categoryExporter.export(categories);
    }

    @Override
    public ByteArrayInputStream exportUsersToExcel(List<UserResponseDTO> users) throws IOException {
        return userExporter.export(users);
    }

    /**
     * Orders tem lógica especial devido aos items aninhados.
     * Cada order pode gerar múltiplas linhas (uma por item).
     */
    @Override
    public ByteArrayInputStream exportOrdersToExcel(List<OrderResponseDTO> orders) throws IOException {
        String[] columns = {
                "ID Orden", "Número de Orden", "Fecha", "Proveedor",
                "Proveedor-ID", "Total Orden", "Producto", "Cantidad", "Precio Unitario"
        };

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Órdenes");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Data rows com lógica especial para items
            int rowIdx = 1;
            for (OrderResponseDTO order : orders) {
                List<OrderItemResponseDTO> items = order.getItems();

                if (items == null || items.isEmpty()) {
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
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
