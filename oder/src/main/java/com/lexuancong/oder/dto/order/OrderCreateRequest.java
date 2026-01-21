package com.lexuancong.oder.dto.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.*;
import com.lexuancong.oder.dto.orderitem.OrderItemCreateRequest;
import com.lexuancong.oder.dto.shippingaddress.ShippingAddressCreateRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateRequest(
        @NotNull Long checkoutId,
        @NotBlank String email,
        @NotNull DeliveryMethod deliveryMethod,
        String note,
        int numberItem,
        @NotNull BigDecimal totalPrice,
        @NotNull List<OrderItemCreateRequest> orderItemCreateRequests,
        @NotNull ShippingAddressCreateRequest shippingAddressCreateRequest,
        @NotNull PaymentMethod paymentMethod


        ) {
    public Order toModel(){
        ShippingAddress shippingAddress = this.shippingAddressCreateRequest.toShippingAddress();
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
                .paymentStatus(PaymentStatus.PENDING)
                .build();

    }


}
