import React from "react";
import {AppProvider} from "@/context/appContext";

function App({children}:React.PropsWithChildren){
    return (
        <AppProvider>

        </AppProvider>
    )
}
export default App;