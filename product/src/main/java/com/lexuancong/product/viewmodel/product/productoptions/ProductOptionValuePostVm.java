package com.lexuancong.product.viewmodel.product.productoptions;

import com.lexuancong.product.viewmodel.product.databinding.ProductOptionPropertyRequire;

import java.util.List;

// đưa lên gia tri cho các option như kích thước màu sắc cho sp
public record ProductOptionValuePostVm(
        Long productOptionId,
        List<String> values
) implements ProductOptionPropertyRequire {

}
