package com.thaovo.shoppingcart.admin.model.mapping;

import com.thaovo.shoppingcart.IMapper;
import com.thaovo.shoppingcart.admin.model.dto.UserResponseDto;
import com.thaovo.shoppingcart.user.authentication.entity.UserAuthEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserResponseMapper implements IMapper<UserAuthEntity, UserResponseDto> {
    @Override
    @Mapping(source = "createdDate", target = "createdAt")
    @Mapping(source = "updatedDate", target = "updatedAt")
    @Mapping(target = "role", expression = "java(mapRole(userAuthEntity))")
    public abstract UserResponseDto toDTO(UserAuthEntity userAuthEntity);

    protected String mapRole(UserAuthEntity userAuthEntity) {
        return userAuthEntity.getAuthorities().stream().findFirst().get().getName();
    }
}
