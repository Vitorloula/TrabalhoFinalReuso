package com.sebastian.inventory_management.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.sebastian.inventory_management.DTO.Category.CategoryResponseDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;
import com.sebastian.inventory_management.DTO.User.UserResponseDTO;

public interface IExportExcelService {
    ByteArrayInputStream exportProductsToExcel(List<ProductResponseDTO> products) throws IOException;
    ByteArrayInputStream exportSuppliersToExcel(List<SupplierResponseDTO> suppliers) throws IOException;
    ByteArrayInputStream exportCategoriesToExcel(List<CategoryResponseDTO> categories) throws IOException;
    ByteArrayInputStream exportOrdersToExcel(List<OrderResponseDTO> orders) throws IOException;
    ByteArrayInputStream exportUsersToExcel(List<UserResponseDTO> users) throws IOException;
}

