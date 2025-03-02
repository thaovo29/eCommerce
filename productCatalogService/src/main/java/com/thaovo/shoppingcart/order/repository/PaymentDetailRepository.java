package com.thaovo.shoppingcart.order.repository;

import com.thaovo.shoppingcart.order.model.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {
}
