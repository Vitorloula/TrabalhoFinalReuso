package com.sebastian.inventory_management.service.export;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;

/**
 * Exportador de produtos para Excel.
 */
@Component
public class ProductExcelExporter extends AbstractExcelExporter<ProductResponseDTO> {

    @Override
    protected String getSheetName() {
        return "Productos";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[] {
                "ID", "Nombre", "Descripción", "Precio", "Stock",
                "Categoría", "Categoria-ID", "Proveedor", "Proveedor-ID"
        };
    }

    @Override
    protected void fillRow(Row row, ProductResponseDTO product) {
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
}
