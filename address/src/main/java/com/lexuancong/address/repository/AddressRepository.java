package com.lexuancong.address.repository;

import com.lexuancong.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByIdIn(List<Long> ids);

    List<Long> id(Long id);
}
