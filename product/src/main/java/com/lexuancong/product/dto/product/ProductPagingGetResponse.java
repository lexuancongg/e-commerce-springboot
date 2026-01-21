package com.lexuancong.product.dto.product;

import java.util.List;

public record ProductPagingGetResponse(
        List<ProductPreviewGetResponse> productPreviewPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast

) {
}
