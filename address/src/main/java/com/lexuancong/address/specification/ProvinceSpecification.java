package com.lexuancong.address.specification;

import com.lexuancong.address.model.Province;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

public class ProvinceSpecification {

//
//    @Query(value = "select province " +
//            "from Province province " +
//            "where province.country.id =: countryId or :countryId is null " +
//            "order by province.name ASC"
//    )
    public static Specification<Province> getProvincesByCountryId( Long countryId) {
        return ((root, query, criteriaBuilder) -> {
            if (countryId == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("country").get("id"), countryId);

        });
    }
}
