package com.lexuancong.product.dto.product;

import java.util.List;

public record ProductPreviewPagingGetResponse(
        List<ProductPreviewGetResponse> productPreviewsPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {

}
