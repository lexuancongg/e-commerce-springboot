import {CategoryVm} from "@/models/category/CategoryVm";
import apiClient from "@/utils/api/apiClient";

class CategoryService{


    public async getCategories():Promise<CategoryVm[]>{
        const response = await apiClient.get(`api/product/customer/categories`);
        if(response.ok){
            return await response.json();
        }
        throw response;

    }

}
export default new CategoryService();