import Head from "next/head";
import React from "react";

// children : component of routing
export default function  Layout({children}:React.PropsWithChildren){
    return (
        <>
            <Head>
                <title>XuanCong -  store</title>
            </Head>


        </>
    )

}