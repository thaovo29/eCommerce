package com.thaovo.shoppingcart.product.repo;

import com.thaovo.shoppingcart.product.entity.Product;
import com.thaovo.shoppingcart.product.repo.custom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom {
    Page<Product> findAll(Pageable pageable );
}
