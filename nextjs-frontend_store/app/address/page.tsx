'use client'
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";
import React, { JSX, useState } from "react";
import { AddressVm } from "@/models/address/AddressVm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import { BiPlusMedical } from "react-icons/bi";
import CardAddress from "@/components/address/cardAddress";

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
    const [addressWantDeleteId , setAddressWantDeleteId] = useState<number>(0);
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
                                <CardAddress address={address}></CardAddress>
                            )
                        })
                }
                </div>

            </ProfileLayoutComponent>
        </>
    );
}
export default MyAddress