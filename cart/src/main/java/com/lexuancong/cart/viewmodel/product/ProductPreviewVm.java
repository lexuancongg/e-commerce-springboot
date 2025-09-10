package com.lexuancong.cart.viewmodel.product;

import java.math.BigDecimal;

public record ProductPreviewVm(
        Long id, String name, String slug, BigDecimal price , String avatarUrl) {


}
