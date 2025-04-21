import apiClient from "@/utils/api/apiClient";
import {RatingPagingVm} from "@/models/rating/RatingPagingVm";
import {rating_paging_demo_data} from "@/demo_data/rating/rating_paging_demo_data";

class RatingService{
    private baseUrl: string
    constructor() {
        this.baseUrl = '/api'
    }

    public async getAverageStarByProductId(productId: number):Promise<number>{
        const response = await apiClient.get('/api');
        if(response.ok) return response.json();
        return 4.6;
        throw response;
    }

    public async getRatingsByProductId(productId: number , pageIndex: number, pageSize : number ):Promise<RatingPagingVm>{
        const response  = await  apiClient.get('/api');
        if(response.ok) return response.json();
        return rating_paging_demo_data;
        throw response;
    }


}
export default new RatingService();