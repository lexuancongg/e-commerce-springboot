package com.lexuancong.feedback.service;

import com.lexuancong.feedback.constants.Constants;
import com.lexuancong.feedback.model.Feedback;
import com.lexuancong.feedback.repostitory.FeedbackRepository;
import com.lexuancong.feedback.service.internal.CustomerService;
import com.lexuancong.feedback.service.internal.OrderService;
import com.lexuancong.feedback.viewmodel.customer.CustomerVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPagingVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPostVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackVm;
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
    private final CustomerService customerService;
    private final OrderService orderService;
    public FeedbackVm createFeedback(FeedbackPostVm feedbackPostVm){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        boolean exitedRating = this.feedbackRepository.existsByCreatedByAndProductId(customerId, feedbackPostVm.productId());
        if(exitedRating){
            throw new IllegalStateException(Constants.ErrorKey.FEEDBACK_EXITED);
        }
        // check xem người dùng đã mua sản  phẩm này chưa
        if(!this.checkUserHasBoughtProductCompleted(feedbackPostVm.productId())){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }

        CustomerVm customerVm = this.customerService.getCustomerInfo();
        if(customerVm == null){
            throw new NotFoundException(Constants.ErrorKey.CUSTOMER_NOT_FOUND,customerId);
        }
        Feedback feedback = new Feedback();
        feedback.setProductId(feedbackPostVm.productId());
        feedback.setContent(feedbackPostVm.content());
        feedback.setStar(feedbackPostVm.star());
        feedback.setLastName(customerVm.lastName());
        feedback.setFirstName(customerVm.firstName());
        Feedback savedFeedback = this.feedbackRepository.save(feedback);
        return FeedbackVm.fromModel(savedFeedback);
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

        public FeedbackPagingVm getRatingByProductId(Long productId , int pageIndex, int pageSize){
            Pageable pageable = PageRequest.of(pageIndex, pageSize , Sort.by("createdAt").descending());
            Page<Feedback> ratingPage = this.feedbackRepository.findAllByProductId(productId, pageable);
            List<Feedback> feedbacks = ratingPage.getContent();
            List<FeedbackVm> feedbackVms = feedbacks.stream()
                    .map(FeedbackVm::fromModel)
                    .toList();
            return new FeedbackPagingVm(
                    feedbackVms, pageIndex,pageSize,
                    (int) ratingPage.getTotalElements(),
                    ratingPage.getTotalPages(),
                    ratingPage.isLast());

        }

    private boolean checkUserHasBoughtProductCompleted(Long productId){
        return  this.orderService.checkUserHasBoughtProductCompleted(productId)
                .hasPurchased();
    }

    public Double getAverageStar(Long productId){
        List<Feedback> feedbacks = this.feedbackRepository.findAllByProductId(productId);
        Double averageStar = feedbacks.stream()
                .mapToDouble(Feedback::getStar)
                .average()
                .orElse(0.0);
        return  averageStar;


    }
}
