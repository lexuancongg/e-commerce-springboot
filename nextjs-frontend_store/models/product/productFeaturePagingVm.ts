import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";

export type ProductFeaturePagingVm = {
    productPreviewsPayload: ProductPreviewVm[],
    pageIndex: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    isLast : boolean


}