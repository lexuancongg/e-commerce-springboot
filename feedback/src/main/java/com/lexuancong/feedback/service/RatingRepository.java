package com.lexuancong.feedback.service;

import com.lexuancong.feedback.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByCreatedByAndProductId(String createdBy, Long productId);

    Page<Rating> findAllByProductId(Long productId, Pageable pageable);
}
