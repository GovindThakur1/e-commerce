package com.govind.ecommerce.service.cart;

import com.govind.ecommerce.dto.CartDto;
import com.govind.ecommerce.model.Cart;
import com.govind.ecommerce.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto mapCartToDto(Cart cart);

}
