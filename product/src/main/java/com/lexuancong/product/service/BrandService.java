package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Brand;
import com.lexuancong.product.repository.BrandRepository;
import com.lexuancong.product.dto.brand.BrandCreateRequest;
import com.lexuancong.product.dto.brand.BrandGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandGetResponse> getBrands(String brandName){
        return brandRepository.findByNameContainingIgnoreCase(brandName)
                .stream()
                .map(BrandGetResponse::fromBrand)
                .toList();
    }

    public BrandGetResponse createBrand(BrandCreateRequest brandCreateRequest) {
        this.validateDuplicateName(brandCreateRequest.name(),null);
        return BrandGetResponse.fromBrand(brandRepository.save(brandCreateRequest.toBrand()));
    }
    private void validateDuplicateName(String brandName,Long id) {
        if(this.checkIsExitsName(brandName,id)){
            throw  new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED,brandName);
        }

    }
    private boolean checkIsExitsName(String  brandName,Long id) {
        return brandRepository.findExistedName(brandName, id)!=null;
    }

    public void updateBrand(Long id, BrandCreateRequest brandCreateRequest){
        this.validateDuplicateName(brandCreateRequest.name(),id);
        Brand brand = brandRepository.findById(id)
                // throw exception
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.BRAND_NOT_FOUND,id));
        brand.setName(brandCreateRequest.name());
        brand.setSlug(brandCreateRequest.slug());
        brand.setPublic(brandCreateRequest.isPublic());
        brandRepository.save(brand);

    }

    public void deleteBrand(Long id){
        Brand brand = brandRepository.findById(id)
                // throw exception
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.BRAND_NOT_FOUND,id));
        if(!brand.getProducts().isEmpty()){
            throw new BadRequestException(Constants.ErrorKey.BRAND_HAS_PRODUCTS,brand.getName());
        }
        brandRepository.deleteById(id);
    }


}
