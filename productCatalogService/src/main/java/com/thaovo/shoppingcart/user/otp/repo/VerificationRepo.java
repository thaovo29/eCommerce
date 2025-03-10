package com.thaovo.shoppingcart.user.otp.repo;

import com.thaovo.shoppingcart.user.otp.entities.OtpVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepo extends JpaRepository<OtpVerificationEntity, Long> {
    Optional<OtpVerificationEntity> findByVerificationToken(String token);
}
