package com.thaovo.shoppingcart.admin.repository;

import com.thaovo.shoppingcart.user.authentication.entity.UserAuthEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    List<UserAuthEntity> findAllByAuthoritiesNotContainingRoleUser();

    Optional<UserAuthEntity> findByAuthoritiesNotContainingRoleUserAndUsername(String username);
}
