package com.lexuancong.feedback.repostitory;

import com.lexuancong.feedback.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByCreatedByAndProductId(String createdBy, Long productId);

    Page<Feedback> findAllByProductId(Long productId, Pageable pageable);

    List<Feedback> findAllByProductId(Long productId);
}
