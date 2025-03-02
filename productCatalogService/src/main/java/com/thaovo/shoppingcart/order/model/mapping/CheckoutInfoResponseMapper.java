package com.thaovo.shoppingcart.order.model.mapping;

import com.thaovo.shoppingcart.IMapper;
import com.thaovo.shoppingcart.cart.model.dto.CartItemResponseDto;
import com.thaovo.shoppingcart.cart.model.dto.ClassificationDto;
import com.thaovo.shoppingcart.order.model.dto.CheckoutInfoResponseDto;
import com.thaovo.shoppingcart.order.model.dto.DeliveryMethodDto;
import com.thaovo.shoppingcart.order.model.dto.PaymentMethodDto;
import com.thaovo.shoppingcart.order.model.entity.OrderEntity;
import com.thaovo.shoppingcart.order.model.entity.OrderItemEntity;
import com.thaovo.shoppingcart.product.mapping.ProductMapper;
import com.thaovo.shoppingcart.user.address.model.mapping.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper(componentModel = "spring")
@Component
public abstract class CheckoutInfoResponseMapper implements IMapper<OrderEntity, CheckoutInfoResponseDto> {
    @Autowired
    protected AddressMapper addressMapper;

    @Autowired
    protected ProductMapper productMapper;

    @Override
    @Mapping(source = "shippingFee" , target = "shoppingFee")
    @Mapping(target = "availablePaymentMethods", expression = "java(getAvailablePaymentMethods())")
    @Mapping(target = "availableDeliveryMethods", expression = "java(getAvailableDeliveryMethods())")
    @Mapping(target = "items", expression = "java(mapOrderItemsToCartItems(orderEntity.getOrderItems()))")
    public abstract CheckoutInfoResponseDto toDTO(OrderEntity orderEntity);

    protected Set<DeliveryMethodDto> getAvailableDeliveryMethods(){
        return Set.of(DeliveryMethodDto.values());
    }
    protected Set<PaymentMethodDto> getAvailablePaymentMethods(){
        return Set.of(PaymentMethodDto.values());
    }
    protected Set<CartItemResponseDto> mapOrderItemsToCartItems (Set<OrderItemEntity> orderItems) {
        return orderItems.stream().map(this::mapOrderItemToCartItem).collect(java.util.stream.Collectors.toSet());
    }

    protected CartItemResponseDto mapOrderItemToCartItem (OrderItemEntity orderItem) {
        var cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProduct(productMapper.toDTO(orderItem.getQuantity().getProduct()));
        cartItemResponseDto.setAmount(orderItem.getAmount());
        ClassificationDto classificationDto = new ClassificationDto();
        classificationDto.setQuantityId(orderItem.getQuantity().getId());
        classificationDto.setColorName(orderItem.getQuantity().getColor().getColorName());
        classificationDto.setSizeName(orderItem.getQuantity().getSize().getSizeName());
        cartItemResponseDto.setClassification(classificationDto);
        return cartItemResponseDto;
    }
}
