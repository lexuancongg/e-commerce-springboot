package com.lexuancong.location.repository;

import com.lexuancong.location.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByIdIn(List<Long> ids);

    List<Long> id(Long id);
}
