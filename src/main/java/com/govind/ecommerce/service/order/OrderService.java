package com.govind.ecommerce.service.order;

import com.govind.ecommerce.dto.OrderDto;
import com.govind.ecommerce.enums.OrderStatus;
import com.govind.ecommerce.exception.ResourceNotFoundException;
import com.govind.ecommerce.model.Cart;
import com.govind.ecommerce.model.Order;
import com.govind.ecommerce.model.OrderItem;
import com.govind.ecommerce.model.Product;
import com.govind.ecommerce.repository.OrderRepository;
import com.govind.ecommerce.repository.ProductRepository;
import com.govind.ecommerce.service.cart.ICartService;
import com.govind.ecommerce.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final IUserService userService;
    private final ModelMapper modelMapper;


    @Override
    public OrderDto placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return mapOrderToDto(savedOrder);
    }


    // helper methods for order creation
    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                })
                .toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem
                        .getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::mapOrderToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getOrdersByUser(Long userId) {
        return Optional.of(userService.getUserById(userId))
                .map(user -> orderRepository.findAllByUserId(user.getId()))
                .map(orders -> orders.stream()
                        .map(this::mapOrderToDto)
                        .toList()
                )
                .orElseThrow(() -> new ResourceNotFoundException("Order/s not found for this user"));
    }

    private OrderDto mapOrderToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
