import {NextPage} from "next";
import {ProductPreview} from "@/models/product/productPreview";

type Props = {
    product: ProductPreview;
    className?:string;
    imageId ?: number;

}
const ProductCard : NextPage = ({product,imageId,className}:Props)=>{

}
export default ProductCard;