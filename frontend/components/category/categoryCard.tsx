import {CategoryVm} from "@/models/category/CategoryVm";
import category from "@/components/home/category";

type  Props = {
    category: CategoryVm,
    handleClick : (slug: string)=> void
}
export default function CategoryCard({category,handleClick}:Props){
    return (
        <div className="category" key={category.id} onClick={() => handleClick(category.slug)}>
            <div className="image-wrapper">
                {category.categoryImage ? (
                    <div
                        className="image"
                        style={{
                            backgroundImage: 'url(' + category.categoryImage.url + ')',
                        }}
                    ></div>
                ) : (
                    <div className="image">No image</div>
                )}
            </div>
            <p style={{fontSize: '14px'}}>{category.name}</p>
        </div>
    )
}