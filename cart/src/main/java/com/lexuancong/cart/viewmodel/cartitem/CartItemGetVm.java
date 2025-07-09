package com.lexuancong.cart.viewmodel.cartitem;

import lombok.Builder;

@Builder
public record CartItemGetVm(String customerId, Long productId, int quantity) {
}
