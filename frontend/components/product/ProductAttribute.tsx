import { ProductDetailVm } from "@/models/product/productDetailVm";

type Props = {
    productDetail: ProductDetailVm;
};

export default function ProductAttribute({ productDetail }: Props) {
    return (
        <div className="container mx-auto mt-16 px-4 space-y-6">
            {productDetail.attributeGroupValues.map((group) => (
                <div key={group.name} className="bg-white rounded-2xl shadow-md p-4 md:p-6">
                    {/* Group Header */}
                    <h2 className="text-lg md:text-xl font-semibold text-gray-800 mb-4 border-b pb-2">
                        {group.name}
                    </h2>

                    {/* Attributes */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
                        {group.attributeValues.map((attr) => (
                            <div
                                key={attr.name}
                                className="bg-gray-50 rounded-xl p-3 flex flex-col justify-center hover:bg-blue-50 transition"
                            >
                                <span className="text-gray-500 text-sm">{attr.name}</span>
                                <span className="text-gray-900 font-medium mt-1">{attr.value}</span>
                            </div>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
}
