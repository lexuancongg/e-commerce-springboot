package com.lexuancong.oder.viewmodel.order;

import com.lexuancong.oder.model.Order;
import com.lexuancong.oder.model.OrderItem;
import com.lexuancong.oder.model.ShippingAddress;
import com.lexuancong.oder.model.enum_status.DeliveryMethod;
import com.lexuancong.oder.model.enum_status.DeliveryStatus;
import com.lexuancong.oder.model.enum_status.OrderStatus;
import com.lexuancong.oder.model.enum_status.PaymentMethod;
import com.lexuancong.oder.viewmodel.orderitem.OrderItemVm;
import com.lexuancong.oder.viewmodel.shippingaddress.ShippingAddressVm;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderVm(
        Long id,
        String email,
        ShippingAddressVm shippingAddressVm,
        String note,
        int numberItem,
        BigDecimal totalPrice,
        OrderStatus oderStatus,
        DeliveryStatus deliveryStatus,
        Set<OrderItemVm> orderItemVms,
        DeliveryMethod deliveryMethod,
        Long checkoutId,
        PaymentMethod paymentMethod
) {
    public static OrderVm fromModel(Order orderSaved, Set<OrderItem> orderItemSet) {
        ShippingAddress shippingAddress = orderSaved.getShippingAddress();
        ShippingAddressVm shippingAddressVm = ShippingAddressVm.fromModel(shippingAddress);

        // tránh lỗi NullPointerException
        Set<OrderItemVm> orderItemVms = Optional.ofNullable(orderItemSet)
                // map xử lý optional
                .map(setOrderItem -> {
                    // map duyệt qua từng ptu
                    return orderItemSet.stream().map(OrderItemVm::fromModel)
                            .collect(Collectors.toSet());
                })
                .orElse(null);


        return new OrderVm(
                orderSaved.getId(),
                orderSaved.getEmail(),
                shippingAddressVm,
                orderSaved.getNote(),
                orderSaved.getNumberItem(),
                orderSaved.getTotalPrice(),
                orderSaved.getOderStatus(),
                orderSaved.getDeliveryStatus(),
                orderItemVms,
                orderSaved.getDeliveryMethod(),
                orderSaved.getCheckoutId(),
                orderSaved.getPaymentMethod()
        );
    }

}
