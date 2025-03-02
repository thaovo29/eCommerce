package com.thaovo.shoppingcart.product.repo;

import com.thaovo.shoppingcart.product.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {
    Optional<Size> findBySizeName(String name);
}
