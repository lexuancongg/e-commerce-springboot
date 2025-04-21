import {RatingPagingVm} from "@/models/rating/RatingPagingVm";

export  const rating_paging_demo_data : RatingPagingVm = {
    ratingsPayload: [
        {
            id: 1,
            content: "Sản phẩm rất tốt!",
            startNumber: 5,
            productId: 101,
            createAt: new Date('2024-04-20T10:00:00Z'),
            lastName: "Nguyễn",
            firstName: "Văn A"
        }
    ],
    pageIndex: 0,
    pageSize: 10,
    totalElements: 30,
    totalPages: 2,
    isLast: false
};