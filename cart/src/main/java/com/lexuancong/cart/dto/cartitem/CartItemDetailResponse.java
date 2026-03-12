package com.lexuancong.cart.dto.cartitem;

import com.lexuancong.cart.dto.productoption.ProductOptionValueResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartItemDetailResponse(
        Long productId,
        int quantity,
        String  productName,
        String slug,
        String avatarUrl,
        BigDecimal price,
        List<ProductOptionValueResponse> productOptionValues

) {
}
