package com.lexuancong.product.viewmodel.productattributevalue;

import com.lexuancong.product.model.ProductAttributeValue;
import jakarta.validation.constraints.NotNull;

public record ProductAttributeValuePostVm(@NotNull Long productId,@NotNull Long productAttributeId, String value) {

}
