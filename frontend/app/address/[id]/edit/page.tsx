'use client'
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { AddressDetailVm } from "@/models/address/AddressDetailVm";
import { useForm } from "react-hook-form";
import addressService from "@/services/address/addressService";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import AddressForm from "@/components/address/addressForm";
import { AddressPostVm } from "@/models/address/AddressPostVm";

const EditAddress = () => {
    const params = useParams();
    const id = params.id;
    const [address, setAddress] = useState<AddressDetailVm>();

    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm<AddressDetailVm>();

    useEffect(() => {
        if (id) {
            addressService.getAddressById(parseInt(id as string))
                .then((responseAddress) => {
                    setAddress(responseAddress)
                }).catch(error => console.log(error))
        }
    }, [id]);
    // khi update
    const onSubmit = (data: AddressDetailVm, event: any) => {
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