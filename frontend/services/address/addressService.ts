import apiClient from "@/utils/api/apiClient";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import {address_demo_data} from "@/demo_data/address/address_demo_data";
import { AddressPostVm } from "@/models/address/AddressPostVm";

class AddressService {
    private baseUrl:string
    constructor() {
        this.baseUrl = "/api/address/customer"
    }



    public async getCountries(){
        const response = await apiClient.get(`${this.baseUrl}`);
        if(response.ok){
            return await response.json();
        }
        throw response;
    }

    public async getProvinces(countryId: number){
        const response = await apiClient.get('/url');
        if(response.ok) return await response.json();
        throw response;
    }

    public async getDistricts(provinceId: number){
        const response = await apiClient.get('/url')
        if(response.ok) return await response.json();
        throw response;
    }

    public async getAddressById(addressId :number):Promise<AddressDetailVm>{
        const response  = await apiClient.get(`${this.baseUrl}/customer/`);
        if(response.ok) return await response.json();
        return address_demo_data[0];
        throw response;

    }
    public async updateAddressById(addressId:number, addressPostVm : AddressPostVm ):Promise<void>{
        const response  = await apiClient.put(`${this.baseUrl}/customer/`);
        if(response.ok) return  await response.json();
        throw response;
    }
}
export default new AddressService();