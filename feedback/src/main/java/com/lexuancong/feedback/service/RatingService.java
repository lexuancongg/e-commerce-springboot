package com.lexuancong.feedback.service;

import com.lexuancong.feedback.model.Rating;
import com.lexuancong.feedback.service.internal.CustomerService;
import com.lexuancong.feedback.viewmodel.customer.CustomerVm;
import com.lexuancong.feedback.viewmodel.rating.RatingPagingVm;
import com.lexuancong.feedback.viewmodel.rating.RatingPostVm;
import com.lexuancong.feedback.viewmodel.rating.RatingVm;
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
    public RatingVm createRating(RatingPostVm ratingPostVm){
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        // check xem có đánh giá của user này chưa
        boolean exitedRating = this.ratingRepository.existsByCreatedByAndProductId(customerId, ratingPostVm.productId());
        if(exitedRating){
            throw new RuntimeException("Exited rating");
        }
        CustomerVm customerVm = this.customerService.getCustomerInfo();
        if(customerVm == null){
            throw new RuntimeException("Customer not found");
        }
        Rating rating = new Rating();
        rating.setCreatedBy(customerId);
        rating.setProductId(ratingPostVm.productId());
        rating.setContent(ratingPostVm.content());
        rating.setFirstName(customerVm.firstName());
        rating.setLastName(customerVm.lastName());
        rating.setStar(ratingPostVm.star());
        Rating savedRating = this.ratingRepository.save(rating);
        return RatingVm.fromModel(savedRating);
    }

    public void deleteRating(Long ratingId){
        // checkxem rating cos ton tai khong
        Rating rating = this.ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        // xem rating nayf cos phair cua chung ta khong
        String customerId = AuthenticationUtils.extractCustomerIdFromJwt();
        String createdBy = rating.getCreatedBy();
        if(!customerId.equals(createdBy)){
            throw new RuntimeException("dont have permission to delete rating");
        }
        this.ratingRepository.deleteById(ratingId);

    }

    public RatingPagingVm getRatingByProductId(Long productId , int pageIndex, int pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize , Sort.by("createdAt").descending());
        Page<Rating> ratingPage = this.ratingRepository.findAllByProductId(productId, pageable);
        List<Rating> ratings = ratingPage.getContent();
        List<RatingVm> ratingVms = ratings.stream()
                .map(RatingVm::fromModel)
                .toList();
        return new RatingPagingVm(
                ratingVms, pageIndex,pageSize,
                (int) ratingPage.getTotalElements(),
                ratingPage.getTotalPages(),
                ratingPage.isLast());

    }
}
