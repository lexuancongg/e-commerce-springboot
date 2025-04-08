package com.lexuancong.cart.viewmodel;

import lombok.Builder;

@Builder
public record CartItemVm(String customerId, Long productId, int quantity) {
}
