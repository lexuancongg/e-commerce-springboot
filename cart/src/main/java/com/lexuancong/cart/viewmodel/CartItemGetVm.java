package com.lexuancong.cart.viewmodel;

import lombok.Builder;

@Builder
public record CartItemGetVm(String customerId,Long productId,int quantity) {
}
