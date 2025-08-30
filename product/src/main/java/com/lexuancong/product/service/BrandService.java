package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Brand;
import com.lexuancong.product.repository.BrandRepository;
import com.lexuancong.product.viewmodel.brand.BrandPostVm;
import com.lexuancong.product.viewmodel.brand.BrandVm;
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

    public List<BrandVm> getBrands(String brandName){
        return brandRepository.findByNameContainingIgnoreCase(brandName)
                .stream()
                .map(BrandVm::fromModel)
                .toList();
    }

    public BrandVm createBrand(BrandPostVm brandPostVm) {
        this.validateDuplicateName(brandPostVm.name(),null);
        return BrandVm.fromModel(brandRepository.save(brandPostVm.toModel()));
    }
    private void validateDuplicateName(String brandName,Long id) {
        if(this.checkIsExitsName(brandName,id)){
            throw  new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED,brandName);
        }

    }
    private boolean checkIsExitsName(String  brandName,Long id) {
        return brandRepository.findExistedName(brandName, id)!=null;
    }

    public void updateBrand(Long id,BrandPostVm brandPostVm){
        this.validateDuplicateName(brandPostVm.name(),id);
        Brand brand = brandRepository.findById(id)
                // throw exception
                .orElseThrow(()->new NotFoundException(Constants.ErrorKey.BRAND_NOT_FOUND,id));
        brand.setName(brandPostVm.name());
        brand.setSlug(brandPostVm.slug());
        brand.setPublic(brandPostVm.isPublic());
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
