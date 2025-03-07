package com.lexuancong.product.viewmodel.attribute;

import java.util.List;

public record ProductAttributePagingVm(
        List<ProductAttributeVm> productAttributePayload,
        int pageNo,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
