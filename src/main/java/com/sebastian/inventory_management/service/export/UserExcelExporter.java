package com.sebastian.inventory_management.service.export;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.sebastian.inventory_management.DTO.User.UserResponseDTO;

/**
 * Exportador de usuários para Excel.
 */
@Component
public class UserExcelExporter extends AbstractExcelExporter<UserResponseDTO> {

    @Override
    protected String getSheetName() {
        return "Usuarios";
    }

    @Override
    protected String[] getColumnHeaders() {
        return new String[] { "ID", "Nombre", "Apellido", "Email", "Rol", "Habilitado" };
    }

    @Override
    protected void fillRow(Row row, UserResponseDTO user) {
        row.createCell(0).setCellValue(user.getId());
        row.createCell(1).setCellValue(user.getName());
        row.createCell(2).setCellValue(user.getLastName());
        row.createCell(3).setCellValue(user.getEmail());
        row.createCell(4).setCellValue(user.getRole().toString());
        row.createCell(5).setCellValue(user.isEnabled() ? "Sí" : "No");
    }
}
