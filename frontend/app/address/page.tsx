'use client'
import React, {JSX, useEffect, useState} from "react";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import {BiPlusMedical} from "react-icons/bi";
import CardAddress from "@/components/address/cardAddress";
import ConfirmationDialog from "@/components/dialog/confirmDialog";
import userAddressService from "@/services/customer/userAddressService";




const MyAddress = (): JSX.Element => {

    const [addresses, setAddresses] = useState<AddressDetailVm[]>([]);
    // show
    const [isShowModelDelete, setIsShowModelDelete] = useState<boolean>(false);
    const [isShowModeConfirmChooseAddressDefaul, setIsShowModelConfirmChooseAddressDefault] = useState<boolean>(false);
    const [currentDefaultAddressId, setCurrentDefaultAddressId] = useState<number>(0);
    const [addressWantDeleteId, setAddressWantDeleteId] = useState<number>(0);
    const [addressWantDefaultId, setAddressWantDefaultId] = useState<number>(0);


    useEffect(() => {
        userAddressService.getDetailAddresses()
            .then( responseDetailAddresses => {
                setAddresses(responseDetailAddresses);
                setCurrentDefaultAddressId(responseDetailAddresses.find((address) => address.isActive)?.id ?? 0)
            })

    }, []);

    // đồng ý xóa địa chỉ
    const handleAgreeDeleteAddressFromModel = () => {
        // gọi api
        if (addressWantDeleteId == 0) return;
        userAddressService.deleteUserAddress(addressWantDeleteId)
            .then(() => {
                setIsShowModelDelete(false);
            })

    }


    // hủy xóa địa chỉ
    const handleCancelDeleteAddressFromModel = () => {
        setIsShowModelDelete(false)

    }


    // đồng ý địa chỉ mặc đingj
    const handleAgreeAddressDefaultFromModel = () => {


    }
    // hủy thay đổi địa chỉ mặc định
    const handleCancelAddressDefaultFromModel = () => {

    }


    // khi click chọn address mặc định
    const handleChooseAddressDefault = (addressId: number) => {
        setIsShowModelConfirmChooseAddressDefault(true);
        setAddressWantDefaultId(addressId);
    }

    const handleChooseDeleteAddress = (addressId: number) => {
        setIsShowModelDelete(true)
        setAddressWantDeleteId(addressId)

    }


    return (
        <>
            <ProfileLayoutComponent menuActive="address" >
                <div className="border border-dashed border-gray-300 p-3 flex justify-center items-center">
                    <button className="flex items-center text-black">
                        <BiPlusMedical className="text-lg"/>
                        <span className="ml-1">Create address</span>
                    </button>
                </div>


                <div className="flex grid-cols-2 flex-wrap gap-4">
                    {
                        addresses.length == 0 ? <> No address</> :
                            addresses.map(address => {
                                return (
                                    <CardAddress
                                        address={address}
                                        handleChooseAddressDefault={handleChooseAddressDefault}
                                        handleChooseDeleteAddress={handleChooseDeleteAddress}


                                    ></CardAddress>
                                )
                            })
                    }
                </div>

            </ProfileLayoutComponent>
            {/* model thông báo xác nhận xóa địa chỉ   */}
            <ConfirmationDialog
                isOpen={isShowModelDelete}
                cancel={handleCancelDeleteAddressFromModel}
                ok={handleAgreeDeleteAddressFromModel}
                cancelText="cancel"
                okText="delete"
            >
                <p>do you want to delete this address</p>
            </ConfirmationDialog>

            {/* model xác nhận thay đổi địa chỉ mặc định */}
            <ConfirmationDialog
                isOpen={isShowModeConfirmChooseAddressDefaul}
                cancel={handleCancelAddressDefaultFromModel}
                ok={handleAgreeAddressDefaultFromModel}
                cancelText="No"
                okText="agree"
            >
                <p>do you want choose this address for default</p>
            </ConfirmationDialog>
        </>
    );
}
export default MyAddress