import {PaymentProvider} from "@/models/payment/PaymentProvider";
import apiClient from "@/utils/api/apiClient";
import {payment_provider_demo_data} from "@/demo_data/payment/payment_provider_demo_data";

class PaymentProviderService{
    private  baseUrl:string ;
    constructor() {
        this.baseUrl = '/api'
    }

    public async getPaymentProviderEnable():Promise<PaymentProvider[]>{
        const response = await apiClient.get('/api');
        if(response.ok) return await response.json();
        return payment_provider_demo_data;
        throw response;

}


}
export default new PaymentProviderService();