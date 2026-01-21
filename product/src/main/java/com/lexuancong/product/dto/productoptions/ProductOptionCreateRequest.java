package com.lexuancong.product.dto.productoptions;

import com.lexuancong.product.model.ProductOption;
import jakarta.validation.constraints.NotBlank;

public record ProductOptionCreateRequest(@NotBlank String name) {
    public ProductOption toProductOption(){
        ProductOption option = new ProductOption();
        option.setName(name);
        return option;
    }
}
