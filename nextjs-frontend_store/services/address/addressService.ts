import apiClient from "@/utils/api/apiClient";

class AddressService {
    private baseUrl: string = '/address/'

    public async deleteUserAddress(addressId: number) {
        const response = await apiClient.delete(`${this.baseUrl}/${addressId}`);
        if(response.ok) return await response.json();
        throw response;
        

    }
}
export default new AddressService();