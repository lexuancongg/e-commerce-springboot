import {Slide} from "@/components/home/slide";
import Category from "@/components/home/category";
import FeaturedProduct from "@/components/home/featuredProduct";

export default function Home(){

    return (
        <div className="homepage">
            <Slide/>
            {/*<Banner></Banner>*/}
            <Category></Category>
            <FeaturedProduct></FeaturedProduct>
        </div>
    );
}