package com.govind.ecommerce.service.cart;

import com.govind.ecommerce.exception.ResourceNotFoundException;
import com.govind.ecommerce.model.Cart;
import com.govind.ecommerce.repository.CartItemRepository;
import com.govind.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cart;
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.getCartItems().clear();
        cartItemRepository.deleteAllByCartId(id); // delete the cart items after clearing
        cart.setTotalAmount(BigDecimal.ZERO); // resetting the total amount to zero
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return getCart(id).getTotalAmount();
    }


    @Override
    public Long initializeNewCart(){
        Cart newCart = new Cart();
//        Long newCartId = cartIdGenerator.incrementAndGet();
//        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }

}
