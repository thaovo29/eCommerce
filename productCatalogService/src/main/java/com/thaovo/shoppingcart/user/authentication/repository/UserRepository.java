package com.thaovo.shoppingcart.user.authentication.repository;

import com.thaovo.shoppingcart.admin.repository.UserRepositoryCustom;
import com.thaovo.shoppingcart.user.authentication.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAuthEntity, Integer>, UserRepositoryCustom {
    Optional<UserAuthEntity> findUserByUsername(String username);
    Optional<UserAuthEntity> findByProfileEmail(String email);
    Optional<UserAuthEntity> findByProfilePhone(String phone);
}
