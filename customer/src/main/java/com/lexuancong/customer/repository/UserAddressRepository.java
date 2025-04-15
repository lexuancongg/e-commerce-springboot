package com.lexuancong.customer.repository;

import com.lexuancong.customer.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
    List<UserAddress> findByUserId(String userId);
    Optional<UserAddress> findByUserIdAndIsActiveTrue(String userId);
    Optional<UserAddress> findOneByUserIdAndAddressId(String userId, Long addressId);
    List<UserAddress> findAllByUserId(String userId);

}
