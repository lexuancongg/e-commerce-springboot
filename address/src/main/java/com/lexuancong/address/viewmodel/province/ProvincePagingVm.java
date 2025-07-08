package com.lexuancong.address.viewmodel.province;

import lombok.Builder;

import java.util.List;
@Builder
public record ProvincePagingVm(
        List<ProvinceGetVm> provincePayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast

) {
}
