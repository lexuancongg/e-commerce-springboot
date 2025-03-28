'use client'
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {useState} from "react";
import {AddressVm} from "@/models/address/AddressVm";
import NavigationComponent from "@/components/common/navigationComponent";
import {Container} from "react-bootstrap";

const navigationPaths : NavigationPathModel[] = [
    {
        pageName:"Home",
        url:'/'
    },
    {
        pageName:'Address',
        url:'#'
    }
]
export default function MyAddress(){
    const [addresses,setAddresses] = useState<AddressVm[]>([]);

    return (
        <>

        </>
    )


}