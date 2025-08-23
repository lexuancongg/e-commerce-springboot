package com.lexuancong.product.viewmodel.product.databinding;

import java.math.BigDecimal;
import java.util.List;

// những thuộc tính cơ ba nhất của sản phẩm bắt buộc phải cs khi thêm sp hay lamf gì đó
public interface BaseProductPropertiesRequire {
    Long id();
    String name();
    String slug();
    String sku();

    String gtin();

//    Double price();
    BigDecimal price();
    Long avatarImageId();

    List<Long> imageIds();






}
