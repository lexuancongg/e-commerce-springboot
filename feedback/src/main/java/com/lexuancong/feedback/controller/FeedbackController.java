package com.lexuancong.feedback.controller;

import com.lexuancong.feedback.service.FeedbackService;
import com.lexuancong.feedback.dto.feedback.FeedbackPagingGetResponse;
import com.lexuancong.feedback.dto.feedback.FeedbackCreateRequest;
import com.lexuancong.feedback.dto.feedback.FeedbackGetResponse;
import com.lexuancong.share.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    // đã check
    @PostMapping("/customer/feedbacks")
    public ResponseEntity<FeedbackGetResponse> createFeedback(@RequestBody FeedbackCreateRequest feedBackCreateRequest) {
        return ResponseEntity.ok(this.feedbackService.createFeedback(feedBackCreateRequest));
    }




    // đã check
    @DeleteMapping("/customer/feedbacks/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable(value = "id") Long feedbackId) {
        this.feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.ok().build();
    }




    // đã check
    @GetMapping("/customer/feedbacks/{productId}")
    public ResponseEntity<FeedbackPagingGetResponse> getRatingByProductId(
            @PathVariable(value = "productId") Long productId,
            @RequestParam(value ="pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER , required = false) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize
            ) {
        return ResponseEntity.ok(this.feedbackService.getRatingByProductId(productId,pageIndex,pageSize));

    }

    // ly số sao để hiển thị trên sp => đã check
    @GetMapping("/customer/feedbacks/{productId}/average-star")
    public ResponseEntity<Double> getAverageStar(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok(this.feedbackService.getAverageStar(productId));
    }



}
