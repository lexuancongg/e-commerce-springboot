package com.lexuancong.feedback.viewmodel.feedback;

import java.util.List;

public record FeedbackPagingVm(
        List<FeedbackVm> ratingPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
