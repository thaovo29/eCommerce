package com.thaovo.shoppingcart.cart.model.entity;

import com.thaovo.shoppingcart.BaseEntity;
import com.thaovo.shoppingcart.product.entity.Quantity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "CART_ITEM")
public class CartItem extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "amount")
    private int amount;

    @OneToOne
    @JoinColumn(name = "quantity_id")
    private Quantity quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        return new EqualsBuilder().append(quantity, cartItem.quantity).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(quantity).toHashCode();
    }
}
