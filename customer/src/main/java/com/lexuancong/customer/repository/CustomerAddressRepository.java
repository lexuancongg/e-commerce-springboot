package com.lexuancong.customer.repository;

import com.lexuancong.customer.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {
    List<CustomerAddress> findByUserId(String userId);
    Optional<CustomerAddress> findByUserIdAndIsActiveTrue(String userId);
    Optional<CustomerAddress> findOneByUserIdAndAddressId(String userId, Long addressId);
    List<CustomerAddress> findAllByUserId(String userId);

}
