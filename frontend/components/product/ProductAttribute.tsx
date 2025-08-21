import {ProductDetailVm}  from "@/models/product/productDetailVm";
import {Fragment} from "react";
type Props = {
    productDetail:ProductDetailVm
}
export default function ProductAttribute({productDetail}:Props){
    return (
        <div className="container mx-auto mt-16 px-4">
            <div className="overflow-x-auto rounded-xl border border-gray-200 shadow-sm">
                <table className="min-w-full text-sm text-left text-gray-700">
                    <tbody>
                    {productDetail.attributeGroupValues.map((group) => (
                        <Fragment key={group.name}>
                            <tr className="bg-gray-100">
                                <th
                                    colSpan={2}
                                    className="px-6 py-3 text-base font-semibold text-gray-800 uppercase tracking-wider"
                                >
                                    {group.name}
                                </th>
                            </tr>
                            {group.attributeValues.map((attr) => (
                                <tr
                                    key={attr.name}
                                    className="border-t border-gray-200 hover:bg-gray-50 transition duration-150"
                                >
                                    <td className="px-6 py-4 font-medium text-gray-600 w-1/3">
                                        {attr.name}
                                    </td>
                                    <td className="px-6 py-4 text-gray-900">{attr.value}</td>
                                </tr>
                            ))}
                        </Fragment>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>

    )
}