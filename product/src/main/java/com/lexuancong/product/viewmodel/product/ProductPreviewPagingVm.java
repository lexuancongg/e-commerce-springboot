package com.lexuancong.product.viewmodel.product;

import java.util.List;

public record ProductPreviewPagingVm(
        List<ProductPreviewVm> productPreviewsPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {

}
