import {FC} from "react";

type Props = {
    isShow : boolean
}
const FilterProduct : FC<Props> = ({isShow})=>{
    return (
        <div
            className={`transition-all duration-300 ease-in-out overflow-hidden ${
                isShow ? 'max-h-[300px]' : 'max-h-0'
            }`}
        >
            <div className="bg-red-50 p-4 grid grid-cols-4 gap-4">
                {/* Sort By */}
                <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-2">Sort By</h4>
                    <select
                        className="w-full border border-gray-300 rounded p-1 text-sm"
                        onChange={(e) => {

                        }}
                    >
                        <option value="default">Default</option>
                        <option value="popularity">Popularity</option>
                        <option value="rating">Average rating</option>
                        <option value="newness">Newness</option>
                        <option value="priceLowHigh">Price: Low to High</option>
                        <option value="priceHighLow">Price: High to Low</option>
                    </select>
                </div>

                {/* Price */}
                <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-2">Price</h4>
                    <ul className="space-y-1">
                        <li className="text-sm text-gray-600 hover:text-black">All</li>
                        <li className="text-sm text-gray-600 hover:text-black">$0.00 - $50.00</li>
                        <li className="text-sm text-gray-600 hover:text-black">$50.00 - $100.00</li>
                        <li className="text-sm text-gray-600 hover:text-black">$100.00 - $150.00</li>
                        <li className="text-sm text-gray-600 hover:text-black">$150.00 - $200.00</li>
                        <li className="text-sm text-gray-600 hover:text-black">$200.00+</li>
                    </ul>
                </div>

                {/* Color */}
                <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-2">Color</h4>
                    <ul className="space-y-1">
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-black rounded-full mr-2"></span> Black
                        </li>
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-blue-500 rounded-full mr-2"></span> Blue
                        </li>
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-gray-400 rounded-full mr-2"></span> Grey
                        </li>
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-green-500 rounded-full mr-2"></span> Green
                        </li>
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-red-500 rounded-full mr-2"></span> Red
                        </li>
                        <li className="flex items-center">
                            <span className="w-4 h-4 bg-white border rounded-full mr-2"></span> White
                        </li>
                    </ul>
                </div>

                {/* Tags */}
                <div>
                    <h4 className="text-sm font-medium text-gray-700 mb-2">Tags</h4>
                    <ul className="space-y-1">
                        <li className="text-sm text-gray-600 hover:text-black">Fashion</li>
                        <li className="text-sm text-gray-600 hover:text-black">Lifestyle</li>
                        <li className="text-sm text-gray-600 hover:text-black">Denim</li>
                        <li className="text-sm text-gray-600 hover:text-black">Streetstyle</li>
                        <li className="text-sm text-gray-600 hover:text-black">Crafts</li>
                    </ul>
                </div>
            </div>
        </div>
    )
}

export default FilterProduct;