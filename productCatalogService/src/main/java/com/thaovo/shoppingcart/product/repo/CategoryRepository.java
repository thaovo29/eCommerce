package com.thaovo.shoppingcart.product.repo;

import com.thaovo.shoppingcart.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
