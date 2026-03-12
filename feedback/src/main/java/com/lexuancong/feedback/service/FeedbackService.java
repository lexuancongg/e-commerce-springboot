package com.lexuancong.feedback.service;

import com.lexuancong.feedback.constants.Constants;
import com.lexuancong.feedback.model.Feedback;
import com.lexuancong.feedback.repostitory.FeedbackRepository;
import com.lexuancong.feedback.service.internal.CustomerClient;
import com.lexuancong.feedback.service.internal.OrderClient;
import com.lexuancong.feedback.dto.customer.CustomerResponse;
import com.lexuancong.feedback.dto.feedback.FeedbackCreateRequest;
import com.lexuancong.feedback.dto.feedback.FeedbackResponse;
import com.lexuancong.share.dto.paging.PagingResponse;
import com.lexuancong.share.exception.AccessDeniedException;
import com.lexuancong.share.exception.IllegalStateException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.utils.AuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final CustomerClient customerClient;
    private final OrderClient orderClient;
    public FeedbackResponse createFeedback(FeedbackCreateRequest feedBackCreateRequest){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        Long productId = feedBackCreateRequest.productId();
        boolean ratingExits = this.feedbackRepository.existsByProductIdAndCustomerId(productId,customerId);
        if(ratingExits){
            throw new IllegalStateException(Constants.ErrorKey.FEEDBACK_EXITED);
        }
        // check xem người dùng đã mua sản  phẩm này chưa
        if(!this.checkUserHasBoughtProductCompleted(productId)){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }

        CustomerResponse customerResponse = this.customerClient.getCustomerInfo();
        if(customerResponse == null){
            throw new NotFoundException(Constants.ErrorKey.CUSTOMER_NOT_FOUND,customerId);
        }
        Feedback feedback = new Feedback();
        feedback.setProductId(feedBackCreateRequest.productId());
        feedback.setContent(feedBackCreateRequest.content());
        feedback.setStar(feedBackCreateRequest.star());
        feedback.setLastName(customerResponse.lastName());
        feedback.setFirstName(customerResponse.firstName());
        Feedback feedbackSaved = this.feedbackRepository.save(feedback);
        return FeedbackResponse.fromFeedback(feedbackSaved);
    }

    public void deleteFeedback(Long feedbackId){
        Feedback feedback = this.feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.FEEDBACK_NOT_FOUND,feedbackId));

        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        String createdBy = feedback.getCreatedBy();
        if(!customerId.equals(createdBy)){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }
        this.feedbackRepository.deleteById(feedbackId);

    }

        public PagingResponse<FeedbackResponse> getRatingByProductId(Long productId , int pageIndex, int pageSize){
            Pageable pageable = PageRequest.of(pageIndex, pageSize , Sort.by("createdAt").descending());
            Page<Feedback> ratingPage = this.feedbackRepository.findAllByProductId(productId, pageable);
            List<Feedback> feedbacks = ratingPage.getContent();
            List<FeedbackResponse> feedbackPayload = feedbacks.stream()
                    .map(FeedbackResponse::fromFeedback)
                    .toList();
            return PagingResponse.<FeedbackResponse>builder()
                    .pageSize(pageSize)
                    .last(ratingPage.isLast())
                    .pageIndex(pageIndex)
                    .totalElements(ratingPage.getTotalElements())
                    .totalPages(ratingPage.getTotalPages())
                    .payload(feedbackPayload)
                    .build();

        }

    private boolean checkUserHasBoughtProductCompleted(Long productId){
        return  this.orderClient.checkUserHasBoughtProductCompleted(productId)
                .hasPurchased();
    }

    public Double getAverageStar(Long productId){
        List<Feedback> feedbacks = this.feedbackRepository.findAllByProductId(productId);
        return feedbacks.stream()
                .mapToDouble(Feedback::getStar)
                .average()
                .orElse(0.0);


    }
}
