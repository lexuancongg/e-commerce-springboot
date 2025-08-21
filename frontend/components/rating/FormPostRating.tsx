import {FC, useState} from "react";
import {useForm} from "react-hook-form";
import StarRatings from 'react-star-ratings';

import {FiledRatingForm} from "@/models/rating/FiledRatingForm";
import Star from "@/components/rating/star/star";
type Props = {
    handleCreateRating : ()=> void


}
const FormPostRating : FC<Props> = (
    {
        handleCreateRating
    }
)=>{
    const [contentRating , setContentRating] = useState<string>('');
    const [ratingStar, setRatingStar] = useState<number>(5);
    const handleChangeRating = ()=>{
    }
    const {formState:{errors}, register,handleSubmit} = useForm<FiledRatingForm>()
    return (
        <>
            <form onSubmit={handleSubmit(handleCreateRating)}>
                <h4>Add a review</h4>

                <div className="d-flex">
                    <p>Your rating: </p>
                    <span className="ms-2">
            <StarRatings
                rating={ratingStar}
                starRatedColor="#FFBF00"
                numberOfStars={5}
                starDimension="16px"
                starSpacing="1px"
                changeRating={handleChangeRating}
            />
          </span>
                </div>

                <div>
          <textarea
              {...register('content', { required: true })}
              onChange={(e) => setContentRating(e.target.value)}
              value={contentRating}
              placeholder="Great..."
              style={{
                  width: '100%',
                  minHeight: '100px',
                  border: '1px solid lightgray',
                  padding: 10,
              }}
          />
                    {errors.content && <p className="text-danger">Content review is required.</p>}
                </div>

                <div className="d-flex justify-content-end m-3">
                    <button type="submit" className="btn btn-primary" style={{ width: '100px' }}>
                        Post
                    </button>
                </div>
            </form>
        </>
    );

}
export default FormPostRating;