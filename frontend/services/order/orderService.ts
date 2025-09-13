import {OrderStatus} from "@/models/order/OrderStatus";
import {OrderVm} from "@/models/order/OrderVm";
import apiClient from "@/utils/api/apiClient";
import {CheckoutVm} from "@/models/order/checkout/CheckoutVm";
import {checkoutData} from "@/demo_data/order/checkout_demo_data";

class OrderService {
    private baseUrl : string;
    constructor() {
        this.baseUrl = `/api/orders/customer/checkouts/`
    }

    public  async getMyOrder(orderStatus : OrderStatus | null = null) :Promise<OrderVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/my-orders?orderStatus=${orderStatus}`)
        if(response.ok) return  await response.json();  // json() method của đối tượng response để chuyển đổi thuộc tính body trả ve
        throw  response;
    }


    public async getCheckoutById(checkoutId:number):Promise<CheckoutVm>{
        const response = await apiClient.get(`${this.baseUrl}/${checkoutId}}`);
        if(response.ok) {
            return await response.json();
        }
        throw response;
    }

}
export default new OrderService();