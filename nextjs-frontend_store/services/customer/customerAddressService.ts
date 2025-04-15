import {AddressPostVm} from "@/models/address/AddressPostVm";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import apiClient from "@/utils/api/apiClient";
import {address_demo_data} from "@/demo_data/address/address_demo_data";
import {UserAddressVm} from "@/models/customer/UserAddressVm";

class CustomerAddressService {
    private baseUrl: string = '...'
    constructor() {
    }
    public async createCustomerAddress(addressPostVm : AddressPostVm):Promise<UserAddressVm>{
        const response  = await apiClient.post(`${this.baseUrl}/..`,JSON.stringify(addressPostVm));
        if(response.ok) return await response.json();
        throw response;


    }
    public async getDetailAddresses():Promise<AddressDetailVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/customer/addresses`)
        if (response.ok) return  await  response.json();
        return address_demo_data;
        throw response;
    }


}
export default new CustomerAddressService();