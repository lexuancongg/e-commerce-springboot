import {ProductPreviewVm} from "@/models/product/ProductPreviewVm";
import {NextPage} from "next";
import {useEffect, useState} from "react";
import Link from "next/link";
import LoadImageSafe from "@/components/common/loadImageSafe";
import styles from '../../styles/modules/common/productCard.module.css'
import clsx from "clsx";
import {formatPrice} from "@/utils/formatPrice";

type Props = {
    product: ProductPreviewVm;
    className?:string[];
    imageId ?: number;

}
const ProductCard : NextPage<Props> = ({product,imageId,className}:Props)=>{
    const [imageUrl,setImageUrl] = useState<string>(imageId ? '' : product.avatarUrl);
    useEffect(()=>{
        if(imageId){
            // fetach image
        }
    },[imageId])
    return(
        <Link
            className={clsx(
                styles['product'],
                className?.map((item) => styles[item])
            )}
            href={`/products/${product.slug}`}
        >
            <div className={styles['product-card']}>
                <div className={styles['image-wrapper']}>
                    <LoadImageSafe src={imageUrl} alt={product.name} />
                </div>
                <div className={styles['info-wrapper']}>
                    <h3 className={styles['prod-name']}>{product.name}</h3>
                    {/*<div className={styles['rating-sold']}>*/}
                    {/*    <div className={styles['star']}>*/}
                    {/*        0 <i className="bi bi-star-fill"></i>*/}
                    {/*    </div>{' '}*/}
                    {/*    |{' '}*/}
                    {/*    <div className={styles['sold']}>*/}
                    {/*        0 <span>sold</span>*/}
                    {/*    </div>*/}
                    {/*</div>*/}

                    <div className={styles['price']}>{formatPrice(product.price)}</div>

                    <hr />

                    <div className={styles['delivery']}>Fast delivery 2 hours</div>
                </div>
            </div>
        </Link>
    )
}
export default ProductCard;