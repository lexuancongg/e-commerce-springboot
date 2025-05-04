package com.lexuancong.search.viewmodel;

import java.util.List;

public record ProductPagingVm(
        List<ProductPreviewVm> productPreviewPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast

) {
}
