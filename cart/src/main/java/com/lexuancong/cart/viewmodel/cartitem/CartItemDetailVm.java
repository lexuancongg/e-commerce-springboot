package com.lexuancong.cart.viewmodel.cartitem;

import com.lexuancong.cart.viewmodel.productoption.ProductOptionValueVm;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CartItemDetailVm(
        Long productId,
        int quantity,
        String  productName,
        String slug,
        String avatarUrl,
        BigDecimal price,
        List<ProductOptionValueVm> productOptionValue

) {
}
