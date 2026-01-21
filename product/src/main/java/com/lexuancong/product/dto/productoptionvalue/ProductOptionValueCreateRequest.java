package com.lexuancong.product.dto.productoptionvalue;

import com.lexuancong.product.dto.product.databinding.ProductOptionPropertyRequire;

import java.util.List;

// đưa lên gia tri cho các option như kích thước màu sắc cho sp
public record ProductOptionValueCreateRequest(
        Long productOptionId,
        List<String> values
) implements ProductOptionPropertyRequire {

}
