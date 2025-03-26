import banner1 from '../../asset/images/banner/baner-1.png'
import banner2 from '../../asset/images/banner/banner-2.webp'
import banner4 from '../../asset/images/banner/banner-4.webp'
import banner5 from '../../asset/images/banner/banner-5.webp'
import subBanner from '../../asset/images/banner/banner-7.jpg'
import {Carousel, Container} from "react-bootstrap";
import Link from "next/link";
import LoadImageSafe from "@/components/common/loadImageSafe";

const listUrlBanner = [banner1.src, banner2.src, banner4.src, banner5.src]
 const  Banner=()=>{
    return (
        <Container className='home-banner-container'>
            <div className="home-banner-wrapper">
                <div className="main-banner">
                    <Carousel>
                        {listUrlBanner.map((item, index) => (
                            <Carousel.Item key={item}>
                                <Link href="/products" key={item}>
                                    <LoadImageSafe  className="d-block w-100" src={item} ></LoadImageSafe>
                                </Link>
                            </Carousel.Item>
                        ))}

                    </Carousel>
                </div>
                <div className="sub-banner">
                    <Link href="/products">
                        <LoadImageSafe src={subBanner.src}></LoadImageSafe>
                    </Link>
                </div>
            </div>

        </Container>
    )
}
export default Banner;