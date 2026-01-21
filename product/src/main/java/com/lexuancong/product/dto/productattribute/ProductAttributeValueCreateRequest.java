package com.lexuancong.product.dto.productattribute;

import jakarta.validation.constraints.NotNull;

public record ProductAttributeValueCreateRequest(@NotNull Long productId, @NotNull Long productAttributeId, String value) {

}
