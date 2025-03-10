package com.thaovo.shoppingcart.order.model.dto;

import com.thaovo.shoppingcart.cart.model.dto.CartItemResponseDto;
import com.thaovo.shoppingcart.user.address.dto.AddressDto;
import lombok.Data;

import java.util.Set;

@Data
public class CheckoutInfoResponseDto {
    private Set<DeliveryMethodDto> availableDeliveryMethods;
    private Set<PaymentMethodDto> availablePaymentMethods;
    private Set<CartItemResponseDto> items;
    private long total;
    private long shoppingFee;
    private double discount;
    private long grandTotal;
}
