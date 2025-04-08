import {CategoryVm} from "@/models/category/CategoryVm";
import apiClient from "@/utils/api/apiClient";
import {category_demo_data} from "@/demo_data/category/category_demo_data";

class CategoryService{
    private baseUrl : string = `${process.env.API_BASE_URL_CATEGORY}`
    constructor() {
    }

    public async getCategories():Promise<CategoryVm[]>{
        const response = await apiClient.get(`${this.baseUrl}/customer/categories`);
        if(response.ok) return await response.json();
        return category_demo_data;
        throw response;

    }

}
export default new CategoryService();