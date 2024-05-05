package com.project.shopApp.services.orderdetails;

import com.project.shopApp.dtos.OrderDetailDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.Order;
import com.project.shopApp.models.OrderDetail;
import com.project.shopApp.models.Product;
import com.project.shopApp.repositories.OrderDetailRepository;
import com.project.shopApp.repositories.OrderRepository;
import com.project.shopApp.repositories.ProductRepository;
import com.project.shopApp.services.orderdetails.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        //tìm xem orderId có tồn tại ko
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find Order with id : "+orderDetailDTO.getOrderId()));
        // Tìm Product theo id
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + orderDetailDTO.getProductId()));
        OrderDetail orderDetail=OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        //luu vao db
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetail with id: "+id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        //Tim xem cac orderDetail co ton tai khon
        OrderDetail existingOrderDetails=orderDetailRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException(("Cannot find orderDetail with id: "+id))
        );
        Order existingOrder=orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find order with id: "+orderDetailDTO.getOrderId())
        );
        Product existingProduct=productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                () -> new DataNotFoundException("Cannot find product with id "+orderDetailDTO.getProductId())
        );
        existingOrderDetails.setOrder(existingOrder);
        existingOrderDetails.setProduct(existingProduct);
        existingOrderDetails.setPrice(orderDetailDTO.getPrice());
        existingOrderDetails.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetails.setColor(orderDetailDTO.getColor());
        existingOrderDetails.setTotalMoney(orderDetailDTO.getTotalMoney());

        return orderDetailRepository.save(existingOrderDetails);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
