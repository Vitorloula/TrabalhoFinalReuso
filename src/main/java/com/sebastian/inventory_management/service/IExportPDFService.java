package com.sebastian.inventory_management.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;

public interface IExportPDFService {
    ByteArrayInputStream exportOrderDetailsToPDF(OrderResponseDTO order) throws IOException;
}