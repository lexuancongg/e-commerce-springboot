package com.lexuancong.product.viewmodel.productattribute;

import jakarta.validation.constraints.NotNull;

public record ProductAttributeValuePostVm(@NotNull Long productId,@NotNull Long productAttributeId, String value) {

}
