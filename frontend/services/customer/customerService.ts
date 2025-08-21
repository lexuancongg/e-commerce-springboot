import {AddressDetailVm} from "@/models/address/AddressDetailVm";
import apiClient from "@/utils/api/apiClient";
import {address_demo_data} from "@/demo_data/address/address_demo_data";
import {CustomerVm} from "@/models/customer/CustomerVm";
import {Profile_demo_data} from "@/demo_data/profile/profile_demo_data";
import {CustomerProfilePutVm} from "@/models/customer/CustomerProfilePutVm";

class CustomerService{
    private baseUrl : string = `${process.env.API_BASE_URL_CUSTOMER}`

    public  async getMyProfile():Promise<CustomerVm>{
        const response = await apiClient.get(`${this.baseUrl}/customer/profile`)
        if(response.ok) return await response.json();
        return Profile_demo_data;
        throw response;
    }



    public async updateCustomerProfile(profileRequest : CustomerProfilePutVm):Promise<any> {
        const response = await apiClient.put(`${this.baseUrl}/customer/profile`,JSON.stringify(profileRequest));
        // 204 : khoong cos body nếu trả về .json thì sẽ bi error
        if(response.status == 204) return response;
        return await response.json();
    }




}
export default new CustomerService();