'use client'
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";
import React, { JSX, useState } from "react";
import { AddressVm } from "@/models/address/AddressVm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import { BiPlusMedical } from "react-icons/bi";
import CardAddress from "@/components/address/cardAddress";
import ConfirmationDialog from "@/components/dialog/confirmDialog";
import addressService from "@/services/address/addressService";

const navigationPaths: NavigationPathModel[] = [
    {
        pageName: "Home",
        url: '/'
    },
    {
        pageName: 'Address',
        url: '#'
    }
]

const addressess: AddressVm[] = [
    {
        id: 1,
        contactName: "Nguyễn Văn A",
        phoneNumber: "0987654321",
        specificAddress: "123 Đường ABC",
        districtId: 10,
        districtName: "Quận Ba Đình",
        provinceId: 1,
        provinceName: "Hà Nội",
        countryId: 84,
        countryName: "Việt Nam",
        isActive: true
    },
    {
        id: 1,
        contactName: "Nguyễn Văn A",
        phoneNumber: "0987654321",
        specificAddress: "123 Đường ABC",
        districtId: 10,
        districtName: "Quận Ba Đình",
        provinceId: 1,
        provinceName: "Hà Nội",
        countryId: 84,
        countryName: "Việt Nam",
        isActive: false
    },
];

const MyAddress = (): JSX.Element => {

    const [addresses, setAddresses] = useState<AddressVm[]>(addressess);
    // show
    const [isShowModelDelete, setIsShowModelDelete] = useState<boolean>(false);
    const [isShowModeConfirmChooseAddressDefaul, setIsShowModelConfirmChooseAddressDefault] = useState<boolean>(false);
    const [currentDefaultAddressId, setCurrentDefaultAddressId] = useState<number>(0);
    const [addressWantDeleteId, setAddressWantDeleteId] = useState<number>(0);
    const [addressWantDefaultId, setAddressWantDefaultId] = useState<number>(0);


    // đồng ý xóa địa chỉ
    const handleAgreeDeleteAddressFromModel = () => {
        // gọi api
        if (addressWantDeleteId == 0) return;
        addressService.deleteUserAddress(addressWantDeleteId)
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
            <ProfileLayoutComponent menuActive="address" navigationPaths={navigationPaths}  >
                <div className="border border-dashed border-gray-300 p-3 flex justify-center items-center">
                    <button className="flex items-center text-black">
                        <BiPlusMedical className="text-lg" />
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