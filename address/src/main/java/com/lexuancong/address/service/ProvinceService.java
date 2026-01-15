package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Province;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.address.specification.ProvinceSpecification;
import com.lexuancong.address.dto.province.ProvincePagingGetResponse;
import com.lexuancong.address.dto.province.ProvinceCreateRequest;
import com.lexuancong.address.dto.province.ProvinceGetResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
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
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final CountryRepository countryRepository;

    public ProvincePagingGetResponse getProvincesPaging(int pageIndex, int pageSize, Long countryId){
        // order khác direction ở chổ order chỉ định cách sx và cả trường còn di... chỉ chỉ định cách sx
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Order.asc("name")));
//        Page<Province> provincePage =provinceRepository.getProvincesPagingByCountryId(countryId,pageable);
        Page<Province> provincePage = this.provinceRepository.findAll(ProvinceSpecification.getProvincesByCountryId(countryId), pageable);
        List<Province> provinces = provincePage.getContent();

        List<ProvinceGetResponse> provinceGetResponses = provinces.stream()
                .map(ProvinceGetResponse::fromProvince)
                .toList();

        return ProvincePagingGetResponse.builder()
                .provincePayload(provinceGetResponses)
                .totalPages(provincePage.getTotalPages())
                .totalElements((int) provincePage.getTotalElements())
                .isLast(provincePage.isLast())
                .pageIndex(provincePage.getNumber())
                .pageSize(provincePage.getSize())
                .build();
    }

    public List<ProvinceGetResponse> getProvincesByCountryId(Long countryId){
        return provinceRepository.findAllByCountryIdOrderByNameAsc(countryId).stream()
                .map(ProvinceGetResponse::fromProvince)
                .toList();

    }

    public ProvinceGetResponse createProvince(final ProvinceCreateRequest provinceCreateRequest){
        Long countryId = provinceCreateRequest.countryId();
        boolean isExistCountryId = countryRepository.existsById(countryId);
        if(!isExistCountryId){
             throw new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, provinceCreateRequest.countryId());
        }
        // nếu cùng quốc gia mà trùng tên thì lỗi
        this.validateExitedName(provinceCreateRequest,null);
        Province province = provinceCreateRequest.toModel(countryRepository.getReferenceById(countryId));
        return ProvinceGetResponse.fromProvince(provinceRepository.save(province));
    }


    private void validateExitedName(ProvinceCreateRequest provinceCreateRequest, Long provinceIdExited ){
        if(this.checkExitedName(provinceCreateRequest,provinceIdExited)){
            throw  new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, provinceCreateRequest.name());
        }
    }
    private boolean checkExitedName(ProvinceCreateRequest provinceCreateRequest, Long provinceIdExited){
        return this.provinceRepository.existsByNameIgnoreCaseAndCountryIdAndIdNot(
                provinceCreateRequest.name(), provinceCreateRequest.countryId(),provinceIdExited
        );
    }





    public void updateProvince(Long id, ProvinceCreateRequest provinceCreateRequest){
        Province province = provinceRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,id));
        // chỉ cho phép trùng tên với cái id chỉnh sửa hiện tại
        this.validateExitedName(provinceCreateRequest, id);
        province.setName(provinceCreateRequest.name());
        province.setType(provinceCreateRequest.type());
        provinceRepository.save(province);

    }
    public void deleteProvince(Long id){
        boolean isExistedProvince = this.provinceRepository.existsById(id);
        if(!isExistedProvince){
            throw  new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,id);
        }
        provinceRepository.deleteById(id);
    }


}
