package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.StockRepository;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.AddressClient;
import com.lexuancong.inventory.dto.address.AddressCreateRequest;
import com.lexuancong.inventory.dto.address.AddressResponse;
import com.lexuancong.inventory.dto.warehouse.WarehouseCreateRequest;
import com.lexuancong.inventory.dto.warehouse.WarehouseResponse;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.exception.ResourceInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final AddressClient addressClient;
    private final StockRepository stockRepository;

    public List<Long> getProductIdsInWarehouse(Long warehouseId) {
        return this.warehouseRepository.getProductIdsInWarehouse(warehouseId);

    }

    public WarehouseResponse createWarehouse(WarehouseCreateRequest warehouseCreateRequest) {
        String wareHouseName = warehouseCreateRequest.name();
        this.validateExitedName(wareHouseName,null);
        AddressCreateRequest addressCreateRequest = this.buildAddressPostVm(warehouseCreateRequest);
        AddressResponse address =
                this.addressClient.createAddress(addressCreateRequest);

        Warehouse warehouse = new Warehouse();
        warehouse.setName(wareHouseName);
        warehouse.setAddressId(address.id());
        return WarehouseResponse.fromWarehouse(this.warehouseRepository.save(warehouse));


    }

    private AddressCreateRequest buildAddressPostVm(WarehouseCreateRequest warehouseCreateRequest){
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
        AddressCreateRequest addressCreateRequest = this.buildAddressPostVm(warehouseCreateRequest);
        this.addressClient.updateAddress(warehouse.getAddressId(), addressCreateRequest);
        this.warehouseRepository.save(warehouse);

    }

    public void  deleteWarehouse(Long id){
        Warehouse warehouse = this.warehouseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND,id));
        if(stockRepository.existsByWarehouse_Id(warehouse.getId())){
            throw  new ResourceInUseException(Constants.ErrorKey.WAREHOUSE_IS_IN_USE);
        }
        this.warehouseRepository.delete(warehouse);
        this.addressClient.deleteAddress(warehouse.getAddressId());
    }

    public List<WarehouseResponse> getWarehouses() {
        List<Warehouse> warehouses = this.warehouseRepository.findAll();
        return warehouses.stream().map(WarehouseResponse::fromWarehouse).toList();

    }
}
