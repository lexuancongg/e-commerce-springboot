package com.lexuancong.feedback.controller;

import com.lexuancong.feedback.service.RatingService;
import com.lexuancong.feedback.viewmodel.rating.RatingPagingVm;
import com.lexuancong.feedback.viewmodel.rating.RatingPostVm;
import com.lexuancong.feedback.viewmodel.rating.RatingVm;
import com.lexuancong.share.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/customer/ratings")
    public ResponseEntity<RatingVm> createRating(@RequestBody RatingPostVm ratingPostVm) {
        return ResponseEntity.ok(this.ratingService.createRating(ratingPostVm));
    }

    @DeleteMapping("/customer/ratings/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable(value = "id") Long ratingId) {
        this.ratingService.deleteRating(ratingId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/customer/ratings/products/{productId}")
    public ResponseEntity<RatingPagingVm> getRatingByProductId(
            @PathVariable(value = "productId") Long productId,
            @RequestParam(value ="pageIndex", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_NUMBER , required = false) int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = Constants.PagingConstants.DEFAULT_PAGE_SIZE , required = false) int pageSize
            ) {
        return ResponseEntity.ok(this.ratingService.getRatingByProductId(productId,pageIndex,pageSize));

    }


}
