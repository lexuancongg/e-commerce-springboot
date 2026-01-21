package com.lexuancong.product.dto.attribute;

import java.util.List;

public record ProductAttributePagingGetResponse(
        List<ProductAttributeGetResponse> productAttributePayload,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
