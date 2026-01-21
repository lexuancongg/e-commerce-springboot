package com.lexuancong.oder.dto.order;

import java.util.List;

public record OrderPagingGetResponse(
        List<OrderPreviewGetResponse> orderPreviewPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
