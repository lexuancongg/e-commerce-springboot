package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.AddressService;
import com.lexuancong.inventory.dto.address.AddressCreateRequest;
import com.lexuancong.inventory.dto.address.AddressGetResponse;
import com.lexuancong.inventory.dto.warehouse.WarehouseCreateRequest;
import com.lexuancong.inventory.dto.warehouse.WarehouseGetResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final AddressService   addressService;

    public List<Long> getProductIdsInWarehouse(Long warehouseId) {
        return this.warehouseRepository.getProductIdsInWarehouse(warehouseId);

    }

    public WarehouseGetResponse createWarehouse(WarehouseCreateRequest warehouseCreateRequest) {
        this.validateExitedName(warehouseCreateRequest.name(),null);
        AddressCreateRequest addressCreateRequest = this.buidAddressPostVm(warehouseCreateRequest);
        AddressGetResponse addressSaved = this.addressService.createAddress(addressCreateRequest);

        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehouseCreateRequest.name());
        warehouse.setAddressId(addressSaved.id());
        return WarehouseGetResponse.fromWarehouse(this.warehouseRepository.save(warehouse));


    }

    private AddressCreateRequest buidAddressPostVm(WarehouseCreateRequest warehouseCreateRequest){
        return  new AddressCreateRequest(
                warehouseCreateRequest.contactName(),
                warehouseCreateRequest.phoneNumber(),
                warehouseCreateRequest.specificAddress(),
                warehouseCreateRequest.districtId(),
                warehouseCreateRequest.provinceId(),
                warehouseCreateRequest.countryId()
        );
    }




    private void validateExitedName(String name,Long warehouseIdExited) {
        if(this.checkExitedName(name,warehouseIdExited)){
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXIST,name);
        }
    }

    private boolean checkExitedName(String name,Long warehouseIdExited) {
        return  this.warehouseRepository.existsByNameAndIdNot(name, warehouseIdExited);
    }


    public void  updateWarehouse(Long id , WarehouseCreateRequest warehouseCreateRequest){
        Warehouse warehouse = this.warehouseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND,id));
        this.validateExitedName(warehouseCreateRequest.name(),id);
        warehouse.setName(warehouseCreateRequest.name());
        AddressCreateRequest addressCreateRequest = this.buidAddressPostVm(warehouseCreateRequest);
        this.addressService.updateAddress(warehouse.getAddressId(), addressCreateRequest);
        this.warehouseRepository.save(warehouse);

    }

    public void  deleteWarehouse(Long id){
        Warehouse warehouse = this.warehouseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND,id));
        this.warehouseRepository.delete(warehouse);
        this.addressService.deleteAddress(warehouse.getAddressId());
    }

    public List<WarehouseGetResponse> getWarehouses() {
        List<Warehouse> warehouses = this.warehouseRepository.findAll();
        return warehouses.stream().map(WarehouseGetResponse::fromWarehouse).toList();

    }
}
