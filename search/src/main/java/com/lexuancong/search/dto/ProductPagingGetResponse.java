package com.lexuancong.search.dto;

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
