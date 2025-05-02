package com.lexuancong.feedback.viewmodel.rating;

import java.util.List;

public record RatingPagingVm(
        List<RatingVm> ratingPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
