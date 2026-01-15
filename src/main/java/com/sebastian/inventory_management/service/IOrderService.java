package com.sebastian.inventory_management.service;

import com.sebastian.inventory_management.DTO.Order.OrderMonthlyDTO;
import com.sebastian.inventory_management.DTO.Order.OrderRequestDTO;
import com.sebastian.inventory_management.DTO.Order.OrderResponseDTO;
import com.sebastian.inventory_management.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderDTO);

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO getOrderByOrderNumber(String orderNumber);

    Page<OrderResponseDTO> searchOrders(String orderNumber, Long supplierId, LocalDate startDate, LocalDate endDate,
            Pageable pageable);

    List<OrderResponseDTO> getAllOrders();

    Page<OrderResponseDTO> getAllOrdersPageable(Pageable pageable);

    List<OrderResponseDTO> getOrdersBySupplier(Long supplierId);

    List<OrderResponseDTO> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderDTO);

    List<Object[]> countOrdersByMonth();

    List<OrderResponseDTO> findRecentOrders();

    List<OrderMonthlyDTO> getOrderCountLastMonths(int months);

    void deleteOrder(Long id);

    Order getOrderByIdEntity(Long id);
}
