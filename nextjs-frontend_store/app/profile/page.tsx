'use client'
import ProfileLayoutComponent from "@/components/common/profileLayout";
import ProfileInfoForm from "@/components/profile/profileInfoForm";
import {useForm} from "react-hook-form";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import {ProfileInfoVm} from "@/models/profile/ProfileInfoVm";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useEffect, useState} from "react";
import customerService from "@/services/customer/customerService";
const navigationPaths: NavigationPathModel[] = [
    {
        pageName: 'Home',
        url: '/',
    },
    {
        pageName: 'Profile',
        url: '#',
    },
];

const Profile = ()=>{
    // ngầm hiểu là kiểu data ProfileInfoVm hoặc undifine
    const [profileInfo , setProfileInfo] = useState<ProfileInfoVm>();
    const onsubmit = (data :ProfileInfoVm,event:any)=>{

    }

    useEffect(() => {
        customerService.getMyProfile()
            .then(responseProfile=>{
                setProfileInfo(responseProfile);
            }).catch(error => console.log(error))
    }, []);
    const {register,handleSubmit,formState: {errors},setValue} = useForm<ProfileInfoVm>();

    return (
        <ProfileLayoutComponent navigationPaths={navigationPaths} menuActive={"profile"}>
            <ProfileInfoForm register={register} handleSubmit={handleSubmit(onsubmit)}
                             errors={errors} profileInfo={profileInfo} setValue={setValue}>

            </ProfileInfoForm>
        </ProfileLayoutComponent>
    )
}
export default Profile;