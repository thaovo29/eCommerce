package com.thaovo.shoppingcart.user.otp.service;

import com.thaovo.shoppingcart.user.authentication.repository.UserRepository;
import com.thaovo.shoppingcart.user.otp.entities.OtpVerificationEntity;
import com.thaovo.shoppingcart.user.otp.repo.VerificationRepo;
import com.thaovo.shoppingcart.user.otp.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationService {
    @Autowired
    private VerificationRepo verificationRepo;

    @Autowired
    private UserRepository userRepository;

    public String createVerificationToken(String target) {
        OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
        otpVerificationEntity.setVerificationToken(Generator.randomString64());
        verificationRepo.save(otpVerificationEntity);
        return otpVerificationEntity.getVerificationToken();
    }

    public boolean verifyToken(String token) {
        Optional<OtpVerificationEntity> verification = verificationRepo.findByVerificationToken(token);
        verification.ifPresent(verificationRepo::delete);
        return verification.isPresent();
    }

    public void setUserAuthenticated(String target) {
    }
}
