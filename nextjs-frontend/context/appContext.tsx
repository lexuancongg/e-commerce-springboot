import React, {Context, PropsWithChildren} from "react";
import {NextPage} from "next";
import {UserInfoProvider} from "@/context/userInfoContext";

export const  AppContext : Context<Object> = React.createContext({});

export function AppProvider({children}: React.PropsWithChildren<{}>){
    return (
        <UserInfoProvider>

        </UserInfoProvider>
    )
}
