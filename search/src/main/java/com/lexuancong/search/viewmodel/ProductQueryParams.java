package com.lexuancong.search.viewmodel;

public record ProductQueryParams(
        String keyword,
        int pageIndex,
        Integer pageSize,
        String brand,
        String category,
        String attribute,
        Double minPrice,
        Double maxPrice
) {
}
