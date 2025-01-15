package com.lexuancong.product.service;

import com.lexuancong.product.repository.BrandRepository;
import com.lexuancong.product.viewmodel.brand.BrandPostVm;
import com.lexuancong.product.viewmodel.brand.BrandVm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandVm> getBrands(String brandName){
        return brandRepository.findByNameContainingIgnoreCase(brandName)
                .stream()
                .map(BrandVm::fromModel)
                .toList();
    }

    public BrandVm createBrand(BrandPostVm brandPostVm) {
        this.validateExitsName(brandPostVm.name(),null);
        return BrandVm.fromModel(brandRepository.save(brandPostVm.toModel()));
    }
    private void validateExitsName(String brandName,Long id) {
        if(this.checkIsExitsName(brandName,id)){
            // throw exception
        }

    }
    private boolean checkIsExitsName(String  brandName,Long id) {
        return brandRepository.findExistedName(brandName, id)!=null;
    }


}
