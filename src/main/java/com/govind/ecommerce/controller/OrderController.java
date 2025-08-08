package com.govind.ecommerce.controller;

import com.govind.ecommerce.dto.OrderDto;
import com.govind.ecommerce.exception.ResourceNotFoundException;
import com.govind.ecommerce.model.Order;
import com.govind.ecommerce.response.ApiResponse;
import com.govind.ecommerce.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.mapOrderToDto(order);
            return ResponseEntity.ok(new ApiResponse("Success", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occurred", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            OrderDto orderDto = orderService.mapOrderToDto(order);
            return ResponseEntity.ok(new ApiResponse("Found", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/order/user/{userId}")
    public ResponseEntity<ApiResponse> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);
            List<OrderDto> orderDtos = orders.stream()
                    .map(orderService::mapOrderToDto)
                    .toList();
            return ResponseEntity.ok(new ApiResponse("Found", orderDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
