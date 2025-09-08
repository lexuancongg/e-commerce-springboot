'use client'
import ProfileLayoutComponent from "@/components/common/profileLayout";
import ProfileInfoForm from "@/components/profile/profileInfoForm";
import {useForm} from "react-hook-form";
import {CustomerVm} from "@/models/customer/CustomerVm";
import {useEffect, useState} from "react";
import customerService from "@/services/customer/customerService";
import {CustomerProfilePutVm} from "@/models/customer/CustomerProfilePutVm";

const Profile = () => {

    const [customer, setCustomer] = useState<CustomerVm>();
    const onsubmit = (data: CustomerProfilePutVm, event: any) => {
        // data : giá trị của các trường mà được register đăng ký
        const profilePutVm: CustomerProfilePutVm = {
            email: event.target.email.value,
            firstName: event.target.firstName.value,
            lastName: event.target.lastName.value
        }
        customerService.updateCustomerProfile(profilePutVm)
            .then((response) => {

            }).catch(error => console.log(error))
    }

    useEffect(() => {
        customerService.getMyProfile()
            .then(responseCustomerVm => {
                setCustomer(responseCustomerVm);
            }).catch(error => console.log(error))
    }, []);

    const {register, handleSubmit, formState: {errors}, setValue} = useForm<CustomerVm>();


    return (
        <ProfileLayoutComponent menuActive={"profile"}>
            <ProfileInfoForm
                register={register}
                handleSubmit={handleSubmit(onsubmit)}

                errors={errors}
                profileInfo={customer}
                setValue={setValue}>

            </ProfileInfoForm>

        </ProfileLayoutComponent>
    )
}
export default Profile;