package com.lexuancong.product.viewmodel.productoptions;

import com.lexuancong.product.model.ProductOption;
import jakarta.validation.constraints.NotBlank;

public record ProductOptionPostVm(@NotBlank String name) {
    public ProductOption toModel(){
        ProductOption option = new ProductOption();
        option.setName(name);
        return option;
    }
}
