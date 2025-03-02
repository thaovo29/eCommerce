package com.thaovo.shoppingcart.order.repository;

import com.thaovo.shoppingcart.order.model.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
