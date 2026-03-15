package com.lexuancong.product.dto.productattribute;

import java.util.List;

public record AttributeGroupValueResponse(String name, List<AttributeValueResponse> attributeValues) {
}
