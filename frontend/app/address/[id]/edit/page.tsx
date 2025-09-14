'use client'
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { AddressDetailVm } from "@/models/address/AddressDetailVm";
import { useForm } from "react-hook-form";
import addressService from "@/services/address/addressService";
import AddressForm from "@/components/address/addressForm";
import { AddressPostVm } from "@/models/address/AddressPostVm";
import ProfileLayoutComponent from "@/components/profile/profileLayout";
import { AddressFormValues } from "@/models/address/AddressFormValues ";

const addressDemo : AddressDetailVm =   {
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
    }

const EditAddress = () => {
    const params = useParams();
    const id = params.id;
    const [address, setAddress] = useState<AddressDetailVm>(addressDemo);

    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm<AddressFormValues>();

    useEffect(() => {
        if (id) {
            addressService.getAddressById(parseInt(id as string))
                .then((resAddress) => {
                    setAddress(resAddress)
                })
                .catch(error => console.log(error))
        }
    }, [id]);



    const onSubmit = (data: AddressFormValues, event: any) => {
        const {
            countryId,
            districtId,
            contactName,
            specificAddress,
            provinceId,
            phoneNumber

        } = data


        const addressPostVm: AddressPostVm = {
            contactName: contactName,
            provinceId: provinceId,
            phoneNumber,
            districtId: districtId,
            specificAddress: specificAddress,
            countryId: countryId
        }
        addressService.updateAddressById(parseInt(String(id)), addressPostVm)
            .then((response) => {
                
            }).catch(error => console.log(error))
    }

    return <ProfileLayoutComponent menuActive={'address'}>
        <AddressForm
            buttonText={'update'}
            addressInit={address}
            register={register}
            handleSubmit={handleSubmit(onSubmit)}
            errors={errors}
            setValue={setValue} 
            isDisplay={true}        >
        </AddressForm>
    </ProfileLayoutComponent>

}
export default EditAddress;