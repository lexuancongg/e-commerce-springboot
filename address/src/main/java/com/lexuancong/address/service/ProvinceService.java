package com.lexuancong.address.service;

import com.lexuancong.address.constants.Constants;
import com.lexuancong.address.model.Province;
import com.lexuancong.address.repository.AddressRepository;
import com.lexuancong.address.repository.CountryRepository;
import com.lexuancong.address.repository.DistrictRepository;
import com.lexuancong.address.repository.ProvinceRepository;
import com.lexuancong.address.specification.ProvinceSpecification;
import com.lexuancong.address.dto.province.ProvinceCreateRequest;
import com.lexuancong.address.dto.province.ProvinceResponse;
import com.lexuancong.share.dto.paging.PagingResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.exception.ResourceInUseException;
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
    private final DistrictRepository districtRepository;
    private final AddressRepository addressRepository;

    public PagingResponse<ProvinceResponse> getProvincesPaging(int pageIndex, int pageSize, Long countryId){
        // order khác direction ở chổ order chỉ định cách sx và cả trường còn di... chỉ chỉ định cách sx
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Order.asc("name")));
        Page<Province> provincePage = this.provinceRepository.findAll(
                ProvinceSpecification.getProvincesByCountryId(countryId), pageable
        );
        List<Province> provinces = provincePage.getContent();

        List<ProvinceResponse> provincePayload = provinces.stream()
                .map(ProvinceResponse::fromProvince)
                .toList();

        return PagingResponse.<ProvinceResponse>builder()
                .payload(provincePayload)
                .totalPages(provincePage.getTotalPages())
                .totalElements(provincePage.getTotalElements())
                .last(provincePage.isLast())
                .pageIndex(provincePage.getNumber())
                .pageSize(provincePage.getSize())
                .build();
    }

    public List<ProvinceResponse> getProvincesByCountryId(Long countryId){
        return provinceRepository.findAllByCountryIdOrderByNameAsc(countryId).stream()
                .map(ProvinceResponse::fromProvince)
                .toList();

    }

    public ProvinceResponse createProvince(final ProvinceCreateRequest provinceCreateRequest){
        Long countryId = provinceCreateRequest.countryId();

        boolean countryExists = countryRepository.existsById(countryId);
        if(!countryExists){
             throw new NotFoundException(Constants.ErrorKey.COUNTRY_NOT_FOUND, countryId);
        }
        this.validateDuplicateName(provinceCreateRequest,null);
        Province province = provinceCreateRequest.toModel(countryRepository.getReferenceById(countryId));
        return ProvinceResponse.fromProvince(provinceRepository.save(province));
    }



    // validate theo quốc gia
    private void validateDuplicateName(ProvinceCreateRequest provinceCreateRequest, Long excludeId ){
        if(this.isDuplicateName(provinceCreateRequest,excludeId)){
            throw  new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, provinceCreateRequest.name());
        }
    }
    private boolean isDuplicateName(ProvinceCreateRequest provinceCreateRequest, Long excludeId){
        return this.provinceRepository.existsByNameIgnoreCaseAndCountryIdAndIdNot(
                provinceCreateRequest.name(),
                provinceCreateRequest.countryId(),
                excludeId
        );
    }





    public void updateProvince(Long id, ProvinceCreateRequest provinceCreateRequest){
        Province province = provinceRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,id));
        this.validateDuplicateName(provinceCreateRequest, id);
        province.setName(provinceCreateRequest.name());
        province.setType(provinceCreateRequest.type());
        provinceRepository.save(province);

    }
    public void deleteProvince(Long id){
        boolean provinceExists = this.provinceRepository.existsById(id);
        if(!provinceExists){
            throw  new NotFoundException(Constants.ErrorKey.PROVINCE_NOT_FOUND,id);
        }
        if(districtRepository.existsByProvince_Id(id)){
            throw new ResourceInUseException(Constants.ErrorKey.PROVINCE_IS_USED_BY_DISTRICT,id);
        }
        if(addressRepository.existsByProvince_Id(id)){
            throw new ResourceInUseException(Constants.ErrorKey.PROVINCE_IS_USED_BY_ADDRESS,id);
        }
        provinceRepository.deleteById(id);
    }


}
