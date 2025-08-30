package com.lexuancong.product.repository;

import com.lexuancong.product.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByNameContainingIgnoreCase(String name);

    @Query(value = "select brand " +
            "from  Brand  brand" +
            " where brand.name =?1 and (?2 is null or ?2 != brand.id) ")
//     cs thể dùng findByNameAndIdNot
    Brand findExistedName(String name,Long id);


}
