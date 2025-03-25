package com.lexuancong.oder.viewmodel;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderPostVm(
        @NotBlank String email,
        String note,
        int numberItem,
        @NotNull BigDecimal totalPrice,
        @NotNull List<OrderItemPostVm> orderItemPostVms,
        @NotNull ShippingAddressPostVm shippingAddressPostVm

        ) {
    public Order toModel(){
        ShippingAddress shippingAddress = this.shippingAddressPostVm.toModel();
        return Order.builder()
                .email(email)
                .note(note)
                .numberItem(numberItem)
                .totalPrice(totalPrice)
                .shippingAddress(shippingAddress)
                .oderStatus(OrderStatus.PENDING)
                .deliveryStatus(DeliveryStatus.PENDING_PICKUP)
                .build();

    }


}
