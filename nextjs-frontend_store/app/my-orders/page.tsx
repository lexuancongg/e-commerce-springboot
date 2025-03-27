'use client'
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {NextPage} from "next";
import {OrderStatus} from "@/models/order/OrderStatus";
import {Container, Tab, Tabs} from "react-bootstrap";
import NavigationComponent from "@/components/common/navigationComponent";
import OrderStatusTab from "@/components/order/orderStatusTab";
import React from "react";

const navigationPaths : NavigationPathModel[] = [
    {
        pageName :'Home',
        url:'#'
    },
    {
        pageName:'My-orders',
        url:'#'
    }
];

const MyOrders : NextPage = ()=>{
    const orderStatus:string[] = Object.keys(OrderStatus)
    return (
        <Container>
            <section className="my-order pt-4">
                <NavigationComponent props={navigationPaths}></NavigationComponent>
            {/*    hiển thị từng tab theo status */}
                <Tabs defaultActiveKey={"ALL"} id={"tab_orders_status"} className={'mb-3'}>
                    <Tab title={"ALL"} eventKey={"ALL"} key={"ALL"}>
                        <OrderStatusTab orderStatus={null}></OrderStatusTab>
                    </Tab>
                    {
                        orderStatus.map(
                            (orderStatus:string ,index:number)=>(
                                <Tab
                                    title={orderStatus}
                                     eventKey={orderStatus}
                                    key={orderStatus}
                                >
                                    {/* ép kiểu trong ts */}
                                    <OrderStatusTab orderStatus={orderStatus as OrderStatus}></OrderStatusTab>
                                </Tab>
                            )
                        )
                    }
                </Tabs>
            </section>
        </Container>
    )
}

export default MyOrders;