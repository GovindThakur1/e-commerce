package com.govind.ecommerce.repository;

import com.govind.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
