import {OrderStatus} from "@/models/order/OrderStatus";
import {OrderVm} from "@/models/order/OrderVm";
import apiClient from "@/utils/api/apiClient";

class OrderService {
    private baseUrl : string;
    constructor() {
        this.baseUrl = '/orders'
    }

    public  async getMyOrder(orderStatus : OrderStatus | null = null) :Promise<OrderVm[]>{
        const res = await apiClient.get(`${this.baseUrl}/my-orders?orderStatus=${orderStatus}`)
        // nếu status response trả về chạy tu 200 đến 299 thì ok() trả về true
        if(res.ok) return  await res.json();  // json() method của đối tượng response để chuyển đổi thuộc tính body trả ve
        throw  res;
    }

}
export default new OrderService();