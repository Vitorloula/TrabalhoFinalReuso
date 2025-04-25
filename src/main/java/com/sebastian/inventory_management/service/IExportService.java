package com.sebastian.inventory_management.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.sebastian.inventory_management.DTO.Product.ProductResponseDTO;
import com.sebastian.inventory_management.DTO.Supplier.SupplierResponseDTO;

public interface IExportService {
    ByteArrayInputStream exportProductsToExcel(List<ProductResponseDTO> products) throws IOException;
    ByteArrayInputStream exportSuppliersToExcel(List<SupplierResponseDTO> products) throws IOException;
}
