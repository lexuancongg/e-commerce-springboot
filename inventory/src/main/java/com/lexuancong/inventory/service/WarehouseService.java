package com.lexuancong.inventory.service;

import com.lexuancong.inventory.constants.Constants;
import com.lexuancong.inventory.model.Warehouse;
import com.lexuancong.inventory.repository.WarehouseRepository;
import com.lexuancong.inventory.service.Internal.AddressService;
import com.lexuancong.inventory.viewmodel.address.AddressPostVm;
import com.lexuancong.inventory.viewmodel.address.AddressVm;
import com.lexuancong.inventory.viewmodel.warehouse.WarehousePostVm;
import com.lexuancong.inventory.viewmodel.warehouse.WarehouseVm;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final AddressService   addressService;

    public List<Long> getProductIdsInWarehouse(Long warehouseId) {
        return this.warehouseRepository.getProductIdsInWarehouse(warehouseId);

    }

    public WarehouseVm createWarehouse(WarehousePostVm warehousePostVm) {
        this.validateExitedName(warehousePostVm.name(),null);
        AddressPostVm addressPostVm = this.buidAddressPostVm(warehousePostVm);
        AddressVm addressSaved = this.addressService.createAddress(addressPostVm);

        Warehouse warehouse = new Warehouse();
        warehouse.setName(warehousePostVm.name());
        warehouse.setAddressId(addressSaved.id());
        return WarehouseVm.fromModel(this.warehouseRepository.save(warehouse));


    }

    private AddressPostVm buidAddressPostVm(WarehousePostVm warehousePostVm){
        return  new AddressPostVm(
                warehousePostVm.contactName(),
                warehousePostVm.phoneNumber(),
                warehousePostVm.specificAddress(),
                warehousePostVm.districtId(),
                warehousePostVm.provinceId(),
                warehousePostVm.countryId()
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


    public void  updateWarehouse(Long id , WarehousePostVm warehousePostVm){
        Warehouse warehouse = this.warehouseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND,id));
        this.validateExitedName(warehousePostVm.name(),id);
        warehouse.setName(warehousePostVm.name());
        AddressPostVm addressPostVm = this.buidAddressPostVm(warehousePostVm);
        this.addressService.updateAddress(warehouse.getAddressId(),addressPostVm);
        this.warehouseRepository.save(warehouse);

    }

    public void  deleteWarehouse(Long id){
        Warehouse warehouse = this.warehouseRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.WAREHOUSE_NOT_FOUND,id));
        this.warehouseRepository.delete(warehouse);
        this.addressService.deleteAddress(warehouse.getAddressId());
    }

    public List<WarehouseVm> getWarehouses() {
        List<Warehouse> warehouses = this.warehouseRepository.findAll();
        return warehouses.stream().map(WarehouseVm::fromModel).toList();

    }
}
