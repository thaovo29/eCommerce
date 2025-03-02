package com.thaovo.shoppingcart.product.repo;

import com.thaovo.shoppingcart.product.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByColorName(String name);
}
