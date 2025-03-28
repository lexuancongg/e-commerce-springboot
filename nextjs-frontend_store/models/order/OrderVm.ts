import {OrderStatus} from "@/models/order/OrderStatus";
import dayjs, {Dayjs} from "dayjs";
import {DeliveryStatus} from "@/models/order/DeliveryStatus";
import {DeliveryMethod} from "@/models/order/DeliveryMethod";
import {OrderItemVm} from "@/models/order/OrderItemVm";

export interface OrderVm{
    id:number;
    orderStatus:OrderStatus;
    totalPrice: number;
    deliveryStatus: DeliveryStatus;
    deliveryMethod: DeliveryMethod;
    orderItemVms: OrderItemVm[];
    createdAt: Dayjs;

}