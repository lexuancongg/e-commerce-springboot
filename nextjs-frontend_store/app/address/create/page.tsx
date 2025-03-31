'use client'
import AddressForm from "@/components/address/addressForm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import { AddressVm } from "@/models/address/AddressVm";
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";
import { useForm } from "react-hook-form";

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
    const onSubmit = ()=>{

    }

    const {register,handleSubmit,formState: {errors}} = useForm<AddressVm>();
    
    return (
        <ProfileLayoutComponent menuActive="address" navigationPaths={navigationPaths}>
            <AddressForm register={register} handleSubmit={handleSubmit(onSubmit)} buttonText="create" errors={errors} address={undefined} ></AddressForm>
        </ProfileLayoutComponent>
    )
}
