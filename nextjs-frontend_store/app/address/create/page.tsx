'use client'
import AddressForm from "@/components/address/addressForm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import { AddressDetailVm } from "@/models/address/AddressDetailVm";
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";
import { useForm } from "react-hook-form";
import {AddressPostVm} from "@/models/address/AddressPostVm";
import customerAddressService from "@/services/customer/customerAddressService";
import {router} from "next/client";

const navigationPaths : NavigationPathModel[] = [
    {
        pageName:"Home",
        url:'#'
    },
    {
        pageName:'Address',
        url:'#'
    },
    {
        pageName:'Create',
        url:'#'
    }
]
export default function CreateAddress (){
    // Partial : biến các thuộc tính của T thành opational
    const onSubmit = (data:AddressDetailVm  , event: any)=>{
        const {
            countryName,
            specificAddress,
            countryId,
            districtName,
            districtId,
            isActive,
            phoneNumber,
            provinceId,
            provinceName,
            id,
            contactName
        } = data;
        const addressPostVm  : AddressPostVm = {
            contactName:contactName!,
            countryId:countryId,
            specificAddress,
            districtId: districtId,
            phoneNumber,
            provinceId:provinceId
        }
        customerAddressService.createCustomerAddress(addressPostVm)
            .then((response)=>{
                router.push('/address')

            }).catch(error=>console.log(error))


    }

    const {register,handleSubmit,formState: {errors},setValue} = useForm<AddressDetailVm>();
    
    return (
        <ProfileLayoutComponent menuActive="address" navigationPaths={navigationPaths}>
            <AddressForm  setValue={setValue} register={register} handleSubmit={handleSubmit(onSubmit)} buttonText="create" errors={errors}  ></AddressForm>
        </ProfileLayoutComponent>
    )
}
