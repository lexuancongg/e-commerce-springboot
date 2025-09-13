package com.lexuancong.product.viewmodel.product;

import com.lexuancong.product.model.Product;

import java.math.BigDecimal;

public record ProductPreviewVm(
        Long id,
        String name,
        String slug,
        BigDecimal price ,
        String avatarUrl) {


}
