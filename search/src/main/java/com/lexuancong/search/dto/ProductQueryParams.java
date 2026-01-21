package com.lexuancong.search.dto;

public record ProductQueryParams(
        String keyword,
        int pageIndex,
        Integer pageSize,
        String category,
        Double minPrice,
        Double maxPrice,
        String brand
) {
}
