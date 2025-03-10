package com.thaovo.shoppingcart.user.profile.repository;

import com.thaovo.shoppingcart.user.profile.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUserUsername(String username);

    Optional<ProfileEntity> findByEmail(String email);

    Optional<ProfileEntity> findByPhone(String phone);

    Optional<ProfileEntity> findById(Long id);

    boolean existsByEmail(String email);
}
