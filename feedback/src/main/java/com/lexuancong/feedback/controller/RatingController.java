package com.lexuancong.feedback.controller;

import com.lexuancong.feedback.service.RatingService;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPagingVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPostVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackVm;
import com.lexuancong.share.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/customer/ratings")
    public ResponseEntity<FeedbackVm> createRating(@RequestBody FeedbackPostVm feedbackPostVm) {
        return ResponseEntity.ok(this.ratingService.createRating(feedbackPostVm));
    }

    @DeleteMapping("/customer/ratings/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable(value = "id") Long ratingId) {
        this.ratingService.deleteRating(ratingId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/customer/ratings/products/{productId}")
    public ResponseEntity<FeedbackPagingVm> getRatingByProductId(
            @PathVariable(value = "productId") Long productId,
            @RequestParam(value ="pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER , required = false) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize
            ) {
        return ResponseEntity.ok(this.ratingService.getRatingByProductId(productId,pageIndex,pageSize));

    }


}
