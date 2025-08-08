package com.govind.ecommerce.service.order;

import com.govind.ecommerce.dto.OrderDto;

import java.util.List;

public interface IOrderService {

    OrderDto placeOrder(Long userId);

    OrderDto getOrder(Long orderId);

    List<OrderDto> getOrdersByUser(Long userId);



}
