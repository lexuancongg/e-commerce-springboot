import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import apiClient from "@/utils/api/apiClient";
import {address_demo_data} from "@/demo_data/address/address_demo_data";
import {ProfileInfoVm} from "@/models/profile/ProfileInfoVm";
import {Profile_demo_data} from "@/demo_data/profile/profile_demo_data";

class CustomerService{
    private baseUrl : string = `${process.env.API_BASE_URL_CUSTOMER}`

    public  async getMyProfile():Promise<ProfileInfoVm>{
        const response = await apiClient.get(`${this.baseUrl}/customer/profile`)
        if(response.ok) return await response.json();
        return Profile_demo_data;
        throw response;
    }
    public async getDetailAddresses():Promise<AddressDetailVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/customer/addresses`)
        if (response.ok) return  await  response.json();
        return address_demo_data;
        throw response;
    }




}
export default new CustomerService();