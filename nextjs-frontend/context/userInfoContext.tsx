import React, {Context, ReactElement, useCallback, useEffect, useMemo, useState} from "react";
import {userInfo} from "node:os";

// khởi tạo context
export const UserInfoContext: Context<UserInfo> = React.createContext({
    firstName: '',
    lastName: '',
    email: '',
    fetchUserInfo: () => {
    }
});

export function UserInfoProvider({children}: React.PropsWithChildren): ReactElement {
    const [email, setEmail] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    useEffect(()=>{
        fetchUserInfo();
    },[])

    // tráng lặp khai báo lại hàm
    const fetchUserInfo: () => void = useCallback(() => {
        CustomerService.getMyProfile()
            .then((userInfo) => {
                setFirstName(userInfo.firstName);
                setLastName(userInfo.lastName);
                setEmail(userInfo.email);
            })
            .catch((err) => {
                console.log(err)
            })
    }, [])

    const userInfo: UserInfo = useMemo(() => (
            {
                firstName,
                lastName,
                email,
                fetchUserInfo
            }
        )
        , [firstName, lastName, email, fetchUserInfo]
    );
    return <UserInfoContext.Provider value={userInfo}>{children}</UserInfoContext.Provider>;

}