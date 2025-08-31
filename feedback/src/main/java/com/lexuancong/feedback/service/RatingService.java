package com.lexuancong.feedback.service;

import com.lexuancong.feedback.constants.Constants;
import com.lexuancong.feedback.model.Feedback;
import com.lexuancong.feedback.service.internal.CustomerService;
import com.lexuancong.feedback.service.internal.OrderService;
import com.lexuancong.feedback.viewmodel.customer.CustomerVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPagingVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackPostVm;
import com.lexuancong.feedback.viewmodel.feedback.FeedbackVm;
import com.lexuancong.share.exception.AccessDeniedException;
import com.lexuancong.share.exception.IllegalStateException;
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
public class RatingService {
    private final RatingRepository ratingRepository;
    private final CustomerService customerService;
    private final OrderService orderService;
    public FeedbackVm createRating(FeedbackPostVm feedbackPostVm){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        // check xem có đánh giá của user này chưa
        boolean exitedRating = this.ratingRepository.existsByCreatedByAndProductId(customerId, feedbackPostVm.productId());
        if(exitedRating){
            throw new IllegalStateException(Constants.ErrorKey.FEEDBACK_EXITED);
        }
        // check xem người dùng đã mua sản  phẩm này chưa
        if(!this.checkUserHasBoughtProductCompleted(feedbackPostVm.productId())){
            throw new AccessDeniedException(Constants.ErrorKey.ACCESS_DENIED);
        }




        CustomerVm customerVm = this.customerService.getCustomerInfo();
        if(customerVm == null){
            throw new RuntimeException("Customer not found");
        }
        Feedback feedback = new Feedback();
        feedback.setCreatedBy(customerId);
        feedback.setProductId(feedbackPostVm.productId());
        feedback.setContent(feedbackPostVm.content());
        feedback.setFirstName(customerVm.firstName());
        feedback.setLastName(customerVm.lastName());
        feedback.setStar(feedbackPostVm.star());
        Feedback savedFeedback = this.ratingRepository.save(feedback);
        return FeedbackVm.fromModel(savedFeedback);
    }

    public void deleteRating(Long ratingId){
        // checkxem rating cos ton tai khong
        Feedback feedback = this.ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        // xem rating nayf cos phair cua chung ta khong
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        String createdBy = feedback.getCreatedBy();
        if(!customerId.equals(createdBy)){
            throw new RuntimeException("dont have permission to delete rating");
        }
        this.ratingRepository.deleteById(ratingId);

    }

    public FeedbackPagingVm getRatingByProductId(Long productId , int pageIndex, int pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize , Sort.by("createdAt").descending());
        Page<Feedback> ratingPage = this.ratingRepository.findAllByProductId(productId, pageable);
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
        return  this.orderService.checkUserHasBoughtProductCompleted(Long productId)
    }
}
