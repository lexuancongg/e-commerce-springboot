import {Slide} from "@/components/home/slide";
import Category from "@/components/home/category";
import FeaturedProduct from "@/components/home/featuredProduct";
import Banner from "@/components/home/banner2";

export default function Home(){

    return (
        <div className="homepage ">
            {/* <Slide/> */}
            <Banner></Banner>
            {/* <Banner></Banner> */}
            <Category></Category>
            <FeaturedProduct></FeaturedProduct>

            {/* <Banner></Banner> */}


        </div>
    );
}