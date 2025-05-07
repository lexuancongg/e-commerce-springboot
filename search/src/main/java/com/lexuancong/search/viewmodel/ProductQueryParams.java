package com.lexuancong.search.viewmodel;

public record ProductQueryParams(
        String keyword,
        int pageIndex,
        Integer pageSize,
        String category,
        Double minPrice,
        Double maxPrice
) {
}
