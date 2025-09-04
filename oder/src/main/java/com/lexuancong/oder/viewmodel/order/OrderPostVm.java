package com.lexuancong.oder.viewmodel.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.*;
import com.lexuancong.oder.viewmodel.orderitem.OrderItemPostVm;
import com.lexuancong.oder.viewmodel.shippingaddress.ShippingAddressPostVm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderPostVm(
        @NotNull Long checkoutId,
        @NotBlank String email,
        @NotNull DeliveryMethod deliveryMethod,
        String note,
        int numberItem,
        @NotNull BigDecimal totalPrice,
        @NotNull List<OrderItemPostVm> orderItemPostVms,
        @NotNull ShippingAddressPostVm shippingAddressPostVm,
        @NotNull PaymentMethod paymentMethod


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
                .deliveryMethod(this.deliveryMethod)
                .checkoutId(checkoutId)
                .paymentStatus()
                .build();

    }


}
