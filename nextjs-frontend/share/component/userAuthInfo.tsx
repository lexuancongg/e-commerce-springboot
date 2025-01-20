"use client"
import {useEffect, useState} from "react";
import apiClient from "@/share/api/apiClient";

type UserAuthenticationInfo = {
    userName : string,
}
type UserAuthenticationInfoVm = {
     userAuthenticationInfo : UserAuthenticationInfo,
     isAuthenticated:boolean
}


function UserAuthInfo(){

    const [userAuthenticationInfoVm , setUserAuthenticationVm] = useState<UserAuthenticationInfoVm>({
        isAuthenticated:false,
        userAuthInfo:{
            userName:''
        }
    });


    async function getAuthenticationInfo():Promise<UserAuthenticationInfoVm> {
          const response  = await apiClient.get('/endPoint')
        return await response.json();

    }

    useEffect(()=>{
        getAuthenticationInfo()
            .then((userAuthInfo)=>{
                setUserAuthenticationVm(userAuthInfo)
            })
    },[])

    return (
        <div>cong</div>
    );


}

export default UserAuthInfo;