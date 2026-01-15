package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.dto.productoption.ProductOptionValueGetResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartItemDetailGetResponse(
        Long productId,
        int quantity,
        String  productName,
        String slug,
        String avatarUrl,
        BigDecimal price,
        List<ProductOptionValueGetResponse> productOptionValue

) {
}
