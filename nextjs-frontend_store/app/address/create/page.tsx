'use client'
import AddressForm from "@/components/address/addressForm";
import ProfileLayoutComponent from "@/components/common/profileLayout";
import { AddressDetailVm } from "@/models/address/AddressDetailVm";
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

    const {register,handleSubmit,formState: {errors},setValue} = useForm<AddressDetailVm>();
    
    return (
        <ProfileLayoutComponent menuActive="address" navigationPaths={navigationPaths}>
            <AddressForm setValue={setValue} register={register} handleSubmit={handleSubmit(onSubmit)} buttonText="create" errors={errors} address={undefined} ></AddressForm>
        </ProfileLayoutComponent>
    )
}
