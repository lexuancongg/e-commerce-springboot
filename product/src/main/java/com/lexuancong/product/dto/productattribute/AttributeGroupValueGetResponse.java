package com.lexuancong.product.dto.productattribute;

import java.util.List;

public record AttributeGroupValueGetResponse(String name, List<AttributeValueGetResponse> attributeValues) {
}
