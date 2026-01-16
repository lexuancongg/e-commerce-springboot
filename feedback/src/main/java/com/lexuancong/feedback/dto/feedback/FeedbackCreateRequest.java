package com.lexuancong.feedback.dto.feedback;

public record FeedbackCreateRequest(
        String content, int star,Long productId
) {
}
