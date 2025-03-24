package com.lexuancong.product.viewmodel.product;

import java.util.List;

public record ProductFeaturePagingVm(
        List<ProductPreviewVm> productPreviewPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {

}
