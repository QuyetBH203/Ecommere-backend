package com.project.shopApp.services.order;

import com.project.shopApp.dtos.OrderDTO;
import com.project.shopApp.exceptions.DataNotFoundException;

import com.project.shopApp.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);

    Page<Order> getOrdersByKeyword(String keyword, PageRequest pageRequest);
}
