"use client"
import {useEffect, useState} from "react";
import apiClient from "@/utils/api/apiClient";
import {Dropdown} from "react-bootstrap";
import Link from "next/link";

type UserAuthenticationInfo = {
    userName: string,
}
type authenticationInfoVm = {
    userAuthenticationInfo: UserAuthenticationInfo,
    isAuthenticated: boolean
}


function UserAuthInfo() {

    const [authenticationInfoVm, setUserAuthenticationVm] = useState<authenticationInfoVm>({
        isAuthenticated: true,
        userAuthenticationInfo: {
            userName: ''
        }
    });


    async function getAuthenticationInfo(): Promise<authenticationInfoVm> {
        const response = await apiClient.get('/endPoint')
        return await response.json();

    }

    useEffect(() => {
        getAuthenticationInfo()
            .then((userAuthInfo) => {
                setUserAuthenticationVm(userAuthInfo)
            })
    }, [])

    return (
        <>
            {authenticationInfoVm.isAuthenticated
                    ?
                (

                    <Dropdown>
                        <Dropdown.Toggle variant="dark" id="user-dropdow " className="bg-transparent"
                                         style={{ border: 'none', color: '#b2b2b2' }}
                        >
                            {authenticationInfoVm.userAuthenticationInfo.userName || 'xuan cong'}
                        </Dropdown.Toggle>
                        <Dropdown.Menu variant="dark" style={{ backgroundColor: '#222' }}>
                            <Dropdown.Item className="d-block h-full">Profile</Dropdown.Item>
                            <Dropdown.Item className="d-block h-full">My orders</Dropdown.Item>
                            <Dropdown.Item className="d-block h-full">Logout</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                )
                    :
                (
                    <div>
                        <Link href="/login" className="d-blockh-full">Login</Link>
                    </div>
                )
            }
        </>
    );


}

export default UserAuthInfo;