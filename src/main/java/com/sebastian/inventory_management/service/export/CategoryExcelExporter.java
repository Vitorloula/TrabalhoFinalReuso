package com.sebastian.inventory_management.service.export;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;

/**
 * Exportador de categorias para Excel.
 */
@Component
public class CategoryExcelExporter extends AbstractExcelExporter<CategoryResponseDTO> {

    @Override
    protected String getSheetName() {
        return "Categorias";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[] { "ID", "Nombre" };
    }

    @Override
    protected void fillRow(Row row, CategoryResponseDTO category) {
        row.createCell(0).setCellValue(category.getId());
        row.createCell(1).setCellValue(category.getName());
    }
}
