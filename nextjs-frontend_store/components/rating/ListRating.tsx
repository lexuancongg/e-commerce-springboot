import {FC, useState} from "react";
import {RatingVm} from "@/models/rating/RatingVm";
import Star from "@/components/rating/star/star";
import ReactPaginate from "react-paginate";
import dayjs from "dayjs";
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);
type  Props= {
    ratings: RatingVm[],
    totalRating:number,
    totalPage:number,
    handlePageChange :({selected}: any)=> void
}
const ListRating : FC<Props> = (
    {
        ratings,
        totalRating,
        totalPage,
        handlePageChange,
    }
)=>{
    const [pageIndex, setPageIndex] = useState<number>(0);

    return (
        <>
            {totalRating == 0 ? (
                <>No reviews for now</>
            ) : (
                <>
                    {ratings?.map((rating: RatingVm) => (
                        <div className="review-item" key={rating.id}>
                            <p style={{ fontWeight: 'bold' }}>
                                {rating.lastName == null && rating.firstName == null ? (
                                    <>Anonymous</>
                                ) : (
                                    <>
                                        {rating.firstName} {rating.lastName}{' '}
                                        <span className="ms-2">
                      <Star star={rating.startNumber} />
                    </span>
                                    </>
                                )}
                            </p>
                            <div className="d-flex justify-content-between">
                                <p className="mx-2">{rating.content}</p>
                                <p className="mx-5">
                                    <span>{dayjs(rating.createAt).fromNow(true)}</span>{' '}
                                    ago
                                </p>
                            </div>
                        </div>
                    ))}
                    {/* PAGINATION */}
                    {totalPage > 1 && (
                        <ReactPaginate
                            forcePage={pageIndex}
                            previousLabel={'Previous'}
                            nextLabel={'Next'}
                            pageCount={totalPage}
                            onPageChange={handlePageChange}
                            containerClassName={'pagination-container'}
                            previousClassName={'previous-btn'}
                            nextClassName={'next-btn'}
                            disabledClassName={'pagination-disabled'}
                            activeClassName={'pagination-active'}
                        />
                    )}
                </>
            )}
        </>
    );
}
export default ListRating;