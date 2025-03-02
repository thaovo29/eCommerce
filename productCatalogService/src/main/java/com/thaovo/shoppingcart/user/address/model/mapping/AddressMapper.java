package com.thaovo.shoppingcart.user.address.model.mapping;

import com.thaovo.shoppingcart.IMapper;
import com.thaovo.shoppingcart.user.address.dto.AddressDto;
import com.thaovo.shoppingcart.user.address.model.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public abstract class AddressMapper implements IMapper<AddressEntity, AddressDto> {
    @Override
    public abstract AddressEntity toEntity(AddressDto addressDto);

    @Override
    public List<AddressEntity> toEntities(List<AddressDto> addressDtos) {
        return addressDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public abstract AddressDto toDTO(AddressEntity addressEntity);

    @Override
    public List<AddressDto> toDTOs(List<AddressEntity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
