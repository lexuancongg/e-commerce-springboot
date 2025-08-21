import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";

export type CheckoutVm = {
    id:number,
    email: string,
    note:string,
    totalAmount:number,
    checkoutItemVms : CheckoutItemVm[]
}