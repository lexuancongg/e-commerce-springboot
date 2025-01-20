import Head from "next/head";
import React from "react";
import Header from "@/share/component/header";
import Footer from "@/share/component/footer";

// children : component of routing
export default function  Layout({children}:React.PropsWithChildren){
    return (
        <>
            <Head>
                <title>XuanCong -  store</title>
            </Head>
            <Header>

            </Header>
            <div className="body">{children}</div>
            <Footer/>

        </>
    )

}