package com.lexuancong.location.service;

import com.lexuancong.location.model.Province;
import com.lexuancong.location.repository.CountryRepository;
import com.lexuancong.location.repository.ProvinceRepository;
import com.lexuancong.location.viewmodel.province.ProvincePagingVm;
import com.lexuancong.location.viewmodel.province.ProvincePostVm;
import com.lexuancong.location.viewmodel.province.ProvinceVm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final CountryRepository countryRepository;

    public ProvincePagingVm getProvincesPaging(int pageIndex,int pageSize,Long countryId){
        // order khác direction ở chổ order chỉ định cách sx và cả trường còn di... chỉ chỉ định cách sx
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Order.asc("name")));
        Page<Province> provincePage =provinceRepository.getProvincesPagingByCountryId(countryId,pageable);
        List<Province> provinces = provincePage.getContent();

        List<ProvinceVm> provinceVms = provinces.stream()
                .map(ProvinceVm::fromModel)
                .toList();

        return ProvincePagingVm.builder()
                .provincePayload(provinceVms)
                .totalPages(provincePage.getTotalPages())
                .totalElements((int) provincePage.getTotalElements())
                .isLast(provincePage.isLast())
                .pageIndex(provincePage.getNumber())
                .pageSize(provincePage.getSize())
                .build();
    }

    public List<ProvinceVm> getProvincesByCountryId(Long countryId){
        return provinceRepository.findAllByCountryIdOrderByNameAsc(countryId).stream()
                .map(ProvinceVm::fromModel)
                .toList();

    }

    public ProvinceVm createProvince(final ProvincePostVm provincePostVm){
        Long countryId = provincePostVm.countryId();
        boolean isExistCountryId = countryRepository.existsById(countryId);
        if(!isExistCountryId){
            // throw  exception
        }
        // nếu cùng quốc gia mà trùng tên thì lỗi
        if(provinceRepository.existsByNameIgnoreCaseAndCountryId(provincePostVm.name(),countryId)){
            // throw exception
        }
        Province province = provincePostVm.toModel(countryRepository.getReferenceById(countryId));
        return ProvinceVm.fromModel(provinceRepository.save(province));
    }

    public void updateProvince(Long id,ProvincePostVm provincePostVm){
        Province province = provinceRepository.findById(id)
                // throw exception here
                .orElseThrow(()->null);
        // chỉ cho phép trùng tên với cái id chỉnh sửa hiện tại
        if(provinceRepository.existsByNameIgnoreCaseAndCountryIdAndIdNot(
                provincePostVm.name(), provincePostVm.countryId(), id)
        ){
            // throw exception
        }
        province.setName(provincePostVm.name());
        province.setType(provincePostVm.type());
        provinceRepository.save(province);

    }
    public void deleteProvince(Long id){
        boolean isExistCountryId = countryRepository.existsById(id);
        if(!isExistCountryId){
            // throw exception
        }
        provinceRepository.deleteById(id);
    }


}
