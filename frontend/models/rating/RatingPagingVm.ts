import {RatingVm} from "@/models/rating/RatingVm";

export type RatingPagingVm = {
    ratingsPayload: RatingVm[],
    pageIndex: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    isLast : boolean
}