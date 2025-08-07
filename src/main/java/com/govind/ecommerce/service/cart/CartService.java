package com.govind.ecommerce.service.cart;

import com.govind.ecommerce.exception.ResourceNotFoundException;
import com.govind.ecommerce.model.Cart;
import com.govind.ecommerce.repository.CartItemRepository;
import com.govind.ecommerce.repository.CartRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cart;
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id); // delete the cart items after clearing
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO); // resetting the total amount to zero
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return getCart(id).getTotalAmount();
    }


}
