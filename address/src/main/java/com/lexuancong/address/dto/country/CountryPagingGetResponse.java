package com.lexuancong.address.dto.country;

import java.util.List;

public record CountryPagingGetResponse(
        List<CountryGetResponse> countryPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
