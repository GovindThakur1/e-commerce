package com.govind.ecommerce.service.cart;

import com.govind.ecommerce.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
}
