import React from "react";



const fetchProductVariations = async (productId : number) : Promise<any>=>{

}

// vì mặc định là server component nên có thể dùng async và fetch api bằng await trực tếp
export default  async function  ProductDetailLayout(
    {children}: { children  : React.ReactNode}
){

    return (
        <div>{children}</div>
    )
}