package com.lexuancong.feedback.dto.feedback;

import java.util.List;

public record FeedbackPagingGetResponse(
        List<FeedbackGetResponse> ratingPayload,
        int pageIndex,
        int pageSize,
        int totalElements,
        int totalPages,
        boolean isLast
) {
}
