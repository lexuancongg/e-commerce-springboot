import {OrderStatus} from "@/models/order/OrderStatus";
import {OrderVm} from "@/models/order/OrderVm";
import apiClient from "@/utils/api/apiClient";
import {CheckoutVm} from "@/models/order/checkout/CheckoutVm";
import {checkoutData} from "@/demo_data/order/checkout_demo_data";

class OrderService {
    private baseUrl : string;
    constructor() {
        this.baseUrl = '/orders'
    }

    public  async getMyOrder(orderStatus : OrderStatus | null = null) :Promise<OrderVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/my-orders?orderStatus=${orderStatus}`)
        // nếu status response trả về chạy tu 200 đến 299 thì ok() trả về true
        if(response.ok) return  await response.json();  // json() method của đối tượng response để chuyển đổi thuộc tính body trả ve
        throw  response;
    }
    public async getCheckoutById(checkout:number):Promise<CheckoutVm>{
        const response = await apiClient.get(`${this.baseUrl}/customer/`);
        if(response.ok) return await response.json();
        return checkoutData;
        throw response;
    }

}
export default new OrderService();