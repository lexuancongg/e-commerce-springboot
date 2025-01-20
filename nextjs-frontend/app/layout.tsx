"use client"
import React from "react";
import {AppProvider} from "@/context/appContext";
import Layout from "@/share/layout/layout";

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