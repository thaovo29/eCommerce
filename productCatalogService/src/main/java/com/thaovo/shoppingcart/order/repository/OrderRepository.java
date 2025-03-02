package com.thaovo.shoppingcart.order.repository;

import com.thaovo.shoppingcart.order.model.entity.OrderEntity;
import com.thaovo.shoppingcart.order.model.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByUserUsernameAndStatus(String username, OrderStatus status);

    List<OrderEntity> findAllByUserUsernameAndStatus(String username, OrderStatus status);
    List<OrderEntity> findAllByUserUsername(String username);

    List<OrderEntity> findAllByStatus(OrderStatus status);

    Optional<OrderEntity> findByIdAndUserUsername(Long id, String username);
}
