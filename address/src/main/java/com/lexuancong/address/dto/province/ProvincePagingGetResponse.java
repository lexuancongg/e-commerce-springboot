package com.lexuancong.address.dto.province;

import lombok.Builder;

import java.util.List;
@Builder
public record ProvincePagingGetResponse(
        List<ProvinceGetResponse> provincePayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast

) {
}
