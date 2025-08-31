package com.lexuancong.feedback.viewmodel.feedback;

import com.lexuancong.feedback.model.Feedback;

import java.time.ZonedDateTime;

public record FeedbackVm(
        Long id, String content , int star, String firstName, String lastName, Long productId, ZonedDateTime createAt
        ) {
    public static FeedbackVm fromModel(Feedback feedback){
        return new FeedbackVm(
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
