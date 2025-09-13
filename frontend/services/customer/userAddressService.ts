import {AddressPostVm} from "@/models/address/AddressPostVm";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import apiClient from "@/utils/api/apiClient";
import {address_demo_data} from "@/demo_data/address/address_demo_data";
import {UserAddressVm} from "@/models/customer/UserAddressVm";

class UserAddressService {
    private baseUrl: string ;
    constructor() {
        this.baseUrl = "/api/customer/customer/user-address"
    }
    public async deleteUserAddress(addressId: number) {
        const response = await apiClient.delete(`${this.baseUrl}/${addressId}`);
        if(response.ok) return await response.json();
        throw response;


    }
    public async createCustomerAddress(addressPostVm : AddressPostVm):Promise<UserAddressVm>{
        const response  = await apiClient.post(`${this.baseUrl}/..`,JSON.stringify(addressPostVm));
        if(response.ok) return await response.json();
        throw response;

    }

    public async getDefaultAddress():Promise<AddressDetailVm>{
        const response = await apiClient.get(`${this.baseUrl}/default`)
        if (response.ok) {
            return await response.json();
        }
        throw response;
    }

    public async getUserAddressDetail():Promise<AddressDetailVm[]>{
        const response = await apiClient.get("/api/customer/customer/addresses");
        if (response.ok){
            return await  response.json();
        }
        throw response;
    }


}
export default new UserAddressService();