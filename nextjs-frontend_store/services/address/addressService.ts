import apiClient from "@/utils/api/apiClient";
import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import {address_demo_data} from "@/demo_data/address/address_demo_data";

class AddressService {
    private baseUrl: string = '/address/'

    public async deleteUserAddress(addressId: number) {
        const response = await apiClient.delete(`${this.baseUrl}/${addressId}`);
        if(response.ok) return await response.json();
        throw response;
        

    }

    public async getCountries(){
        const response = await apiClient.get('/url');
        if(response.ok) return await response.json();
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
    public async updateAddressById(addressId:number):Promise<void>{
        const response  = await apiClient.put(`${this.baseUrl}/customer/`);
        if(response.ok) return  await response.json();
        throw response;
    }
}
export default new AddressService();