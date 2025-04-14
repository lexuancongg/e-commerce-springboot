import React, {FC} from "react";
import {FieldErrors, UseFormRegister, UseFormSetValue} from "react-hook-form";
import {ProfileInfoVm} from "@/models/profile/ProfileInfoVm";
import Input from "@/components/item/input";
type  Props = {
    register: UseFormRegister<ProfileInfoVm>,
    // submit
    handleSubmit: () => void,
    errors: FieldErrors<ProfileInfoVm>,
    // trường hợp có nếu update , còn undefine nếu create
    profileInfo?: ProfileInfoVm
    // xác định tên nút là create hay update
    setValue: UseFormSetValue<ProfileInfoVm>;
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
                        fieldName="userName"
                        disabled={true}
                        defaultValue={profileInfo?.userName}
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

        </>
    )


}
export default ProfileInfoForm;