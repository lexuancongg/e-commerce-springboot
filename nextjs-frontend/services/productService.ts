import {ProductPreviewVm} from "@/models/product/productPreviewVm";
import apiClient from "@/utils/api/apiClient";

class ProductService {
    private base_url:string = '/api';
    public async getProductsByIds(ids : number[]):Promise<ProductPreviewVm[]>{
        const response = await apiClient.get(`${this.base_url}/product...`)
        if(response.ok){
            return  await response.json();
        }
        throw new Error();

    }

}
export default new ProductService();
