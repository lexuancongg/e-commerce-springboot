package com.lexuancong.oder.viewmodel.order;

import java.util.List;

public record OrderPreviewPaging(
        List<OrderPreviewVm> orderPreviewPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
