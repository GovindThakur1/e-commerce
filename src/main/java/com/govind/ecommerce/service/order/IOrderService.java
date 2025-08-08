package com.govind.ecommerce.service.order;

import com.govind.ecommerce.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);

    Order getOrder(Long orderId);

    List<Order> getOrdersByUser(Long userId);



}
