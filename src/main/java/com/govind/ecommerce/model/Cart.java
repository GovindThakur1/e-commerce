package com.govind.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        updateTotalAmount();
    }

    public void removeCartItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        updateTotalAmount();
    }

    public void updateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .map(cartItem -> {
                    BigDecimal unitPrice = cartItem.getUnitPrice();
                    if (unitPrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(new BigDecimal(cartItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}




















