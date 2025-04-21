package com.lexuancong.feedback.service;

import com.lexuancong.feedback.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
