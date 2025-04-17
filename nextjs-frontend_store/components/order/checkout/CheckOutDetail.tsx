import {FC, useState} from "react";
import {OrderItemVm} from "@/models/order/OrderItemVm";
import userAddressService from "@/services/customer/userAddressService";
import {CheckoutItemVm} from "@/models/order/checkout/CheckoutItemVm";
import {formatPrice} from "@/utils/formatPrice";
type Props = {
    checkoutItems :CheckoutItemVm[]
}
const CheckOutDetail :FC<Props> = ({checkoutItems})=>{
    const [totalPrice, setTotalPrice] = useState(0);

    return (
        <div></div>
    );

}
export default CheckOutDetail;