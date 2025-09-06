"use client";

import Carousel from "react-bootstrap/Carousel";
import "bootstrap/dist/css/bootstrap.min.css";

const Banner = () => {
    return (
        <div
            className="relative h-screen w-full "
            style={{
                backgroundImage: `url('https://cdn11.bigcommerce.com/s-2k3abrmqs0/images/stencil/1200w/carousel/262/Banner_DISCOVER_IMPERIAL_31.75cm_x_15.875cm_1200x600_pixels.jpg?c=2')`,
                backgroundSize: "cover",
                backgroundPosition: "center",
            }}
        >
        </div>

    );
};

export default Banner;
