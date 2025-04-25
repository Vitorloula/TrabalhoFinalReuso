package com.sebastian.inventory_management.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.service.IExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportServiceImpl implements IExportService {

    @Override
    public ByteArrayInputStream exportProductsToExcel(List<ProductResponseDTO> products) throws IOException {
        String[] COLUMNs = { "ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría", "Categoria-ID", "Proveedor", "Proveedor-ID" };

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
}
