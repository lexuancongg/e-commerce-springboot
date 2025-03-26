"use client"
import React from "react";
import {AppProvider} from "@/context/appContext";
import Layout from "@/components/share/layout";

import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import '../styles/modules/common/header.css';
import '../styles/modules/share/global.css';
import '../styles/modules/home/components/slide.css';
import '../styles/modules/home/components/category.css';
import '../styles/modules/home/home.css';
import '../styles/modules/home/components/featuredProduct.css'
import '../styles/modules/common/paging.css';
import '../styles/modules/common/footer.css';
import '../styles/modules/cart/cart.css'


function App({children}: React.PropsWithChildren) {
    return (
        <html>
        <body>
        <AppProvider>
            <Layout>
                {children}
            </Layout>
        </AppProvider>
        </body>
        </html>

    )
}

export default App;