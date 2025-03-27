'use client'
import {OrderStatus} from "@/models/order/OrderStatus";
import {useEffect, useState} from "react";
import {OrderVm} from "@/models/order/OrderVm";
import orderService from "@/services/order/orderService";
import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import productService from "@/services/product/productService";
type Props = {
    orderStatus : OrderStatus| null
}
export default function OrderStatusTab({orderStatus}:Props){
    const [orders , setOrders] = useState<OrderVm[]>([])


    useEffect(()=>{
        orderService.getMyOrder(orderStatus)
            .then(async (orderVmResults ) => {
                const productIds : number[] = orderVmResults.flatMap((orderVmResult)=>{
                    return  orderVmResult.orderItemVms.map(orderItem=>{
                        return orderItem.productId;
                    })
                })
                const productPreviewVms : ProductPreviewVm[] =await productService.getProductsByIds(productIds)
                

            })
    },[orderStatus])

    return (

    )
}