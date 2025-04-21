package com.lexuancong.feedback.controller;

import com.lexuancong.feedback.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    @PostMapping("/customer/ratings")


}
