package com.lexuancong.feedback.dto.feedback;

import com.lexuancong.feedback.model.Feedback;

import java.time.ZonedDateTime;

public record FeedbackGetResponse(
        Long id, String content , int star, String firstName, String lastName, Long productId, ZonedDateTime createAt
        ) {
    public static FeedbackGetResponse fromFeedback(Feedback feedback){
        return new FeedbackGetResponse(
                feedback.getId(),
                feedback.getContent(),
                feedback.getStar(),
             feedback.getFirstName(),
                feedback.getLastName(),
                feedback.getProductId(),
                feedback.getCreatedAt()
        );
    }

}
