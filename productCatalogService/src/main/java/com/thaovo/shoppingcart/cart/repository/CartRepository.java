package com.thaovo.shoppingcart.cart.repository;

import com.thaovo.shoppingcart.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAuthEntityUsername(String username);
}
