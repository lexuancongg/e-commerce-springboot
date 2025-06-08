import React, {FC} from "react";
import {FieldErrors, UseFormRegister, UseFormSetValue} from "react-hook-form";
import {CustomerVm} from "@/models/customer/CustomerVm";
import Input from "@/components/item/input";
import Link from "next/link";
type  Props = {
    register: UseFormRegister<CustomerVm>,
    // submit
    handleSubmit: () => void,
    errors: FieldErrors<CustomerVm>,
    // trường hợp có nếu update , còn undefine nếu create
    profileInfo?: CustomerVm
    // xác định tên nút là create hay update
    setValue: UseFormSetValue<CustomerVm>;
}
const ProfileInfoForm : FC<Props> = ({
    errors,
    handleSubmit,
    profileInfo,
    setValue,
    register
                                     })=>{
    return (
        <>
            <div className="row">
                <div className="col-lg-9">
                    <Input
                        labelText="userName"
                        register={register}
                        fieldName="username"
                        disabled={true}
                        defaultValue={profileInfo?.username}
                        registerOptions={{
                            required: {value: true, message: 'this feild is require'}
                        }}
                    />
                </div>

            </div>
            <div className={'row'}>
                <div className="col-lg-9">
                    <Input
                        labelText="firstName"
                        register={register}
                        fieldName="firstName"
                        registerOptions={{
                            required: {value: true, message: 'This feild is required'},
                        }}
                        defaultValue={profileInfo?.firstName}
                    />
                </div>
            </div>
            <div className={'row'}>
                <div className="col-lg-9">
                    <Input
                        labelText="lastName"
                        register={register}
                        fieldName="lastName"
                        registerOptions={{
                            required: {value: true, message: 'This feild is required'},
                        }}
                        defaultValue={profileInfo?.lastName}
                    />
                </div>
            </div>
            <div className={'row'}>
                <div className="col-lg-9">
                    <Input
                        labelText="email"
                        register={register}
                        fieldName="email"
                        registerOptions={{
                            required: {value: true, message: 'This feild is required'},
                        }}
                        defaultValue={profileInfo?.email}
                    />
                </div>
            </div>
            <div className="text-center">
                <button onClick={handleSubmit}  className="btn btn-primary" type="button">
                    Update
                </button>
                <Link href="/">
                    <button className="btn btn-secondary m-3">Cancel</button>
                </Link>
            </div>

        </>
    )


}
export default ProfileInfoForm;