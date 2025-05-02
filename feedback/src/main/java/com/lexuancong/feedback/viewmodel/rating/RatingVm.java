package com.lexuancong.feedback.viewmodel.rating;

import com.lexuancong.feedback.model.Rating;

import java.time.ZonedDateTime;

public record RatingVm(
        Long id, String content , int star, String firstName, String lastName, Long productId, ZonedDateTime createAt
        ) {
    public static RatingVm fromModel(Rating rating){
        return new RatingVm(
                rating.getId(),
                rating.getContent(),
                rating.getStar(),
                rating.getFirstName(),
                rating.getLastName(),
                rating.getProductId(),
                rating.getCreatedAt()
        );
    }

}
