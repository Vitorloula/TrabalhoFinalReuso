package com.sebastian.inventory_management.service.export;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;

/**
 * Exportador de fornecedores para Excel.
 */
@Component
public class SupplierExcelExporter extends AbstractExcelExporter<SupplierResponseDTO> {

    @Override
    protected String getSheetName() {
        return "Proveedores";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[] { "ID", "Nombre", "Email de Contacto", "Numero de Telefono" };
    }

    @Override
    protected void fillRow(Row row, SupplierResponseDTO supplier) {
        row.createCell(0).setCellValue(supplier.getId());
        row.createCell(1).setCellValue(supplier.getName());
        row.createCell(2).setCellValue(supplier.getContactEmail());
        row.createCell(3).setCellValue(supplier.getPhoneNumber());
    }
}
