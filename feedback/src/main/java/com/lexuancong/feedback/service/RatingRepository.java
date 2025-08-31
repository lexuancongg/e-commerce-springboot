package com.lexuancong.feedback.service;

import com.lexuancong.feedback.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Feedback, Long> {
    boolean existsByCreatedByAndProductId(String createdBy, Long productId);

    Page<Feedback> findAllByProductId(Long productId, Pageable pageable);
}
