import { AddressVm } from "@/models/address/AddressVm";
import { FC } from "react";
import { FieldErrors, useForm, UseFormRegister } from "react-hook-form";

type Props = {
    // để đăng ký
    register: UseFormRegister<AddressVm>,
    // submit
    handleSubmit: ()=> void,
    errors: FieldErrors<AddressVm>,
    address? : AddressVm| undefined
    
}
const AddressForm : FC<any> = ()=>{
    
    return (
        
    )

}
export default AddressForm;