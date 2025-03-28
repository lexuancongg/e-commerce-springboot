import { OrderVm } from "@/models/order/OrderVm"
import { formatPrice } from "@/utils/formatPrice";
import dayjs, { Dayjs, isDayjs } from "dayjs";
import Link from "next/link";
import LoadImageSafe from "../common/loadImageSafe";

type Props = {
    order : OrderVm
}

export default function OrderCard({order}: Props){
    const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
    const getOrderDate = (createAt: Dayjs | string)=>{
        if(typeof createAt == "string"){
            // convert từ string sang Dayjs để format 
            const parseDate : Dayjs = dayjs(createAt);
            if(!parseDate.isValid()){
                console.warn(`Invalid date string: ${createAt}`);
                return "Invalid Date";
            }
            return parseDate.format(DATE_TIME_FORMAT);
        }
        return  createAt.format(DATE_TIME_FORMAT);
        
    }


    return (
        // <div className="card mb-5 mt-2" style={{maxWidth: '100%'}} key={order.id}>
        //     <div className="card-header bg-transparent d-flex justify-content-between">
        //         <div className="delivery-method-title">
        //             <span>Delivery Method: {order.deliveryMethod}</span>
        //         </div>
        //         <div className="delivery-status-title">
        //             <span>Delivery Status: {order.deliveryStatus}</span>
        //         </div>
        //         <div className="order-status-title">
        //             <span>Status: {order.orderStatus}</span>
        //         </div>
        //     </div>
        //     <div className="card-body">
        //         {order.orderItemVms.map((item) => (
        //             <div key={item.id} className="order-item d-flex pb-3">
        //                 <Link
        //                     href={{
        //                         pathname: '/redirect',
        //                         query: {productId: item.productId},
        //                     }}
        //                 >
        //                     <LoadImageSafe
        //                         src={item.productAvatarUrl}
        //                         alt={item.productName}
        //                         style={{width: '120px', height: '120px', cursor: 'pointer'}}
        //                     />
        //                 </Link>
        //                 <div className="align-self-center p-4">
        //                     <div className="item-product-name">
        //                         <Link
        //                             href={{
        //                                 pathname: '/redirect',
        //                                 query: {productId: item.productId},
        //                             }}
        //                         >
        //                             {item.productName}
        //                         </Link>
        //                     </div>
        //                     <div>
        //                         <span style={{fontWeight: 600}}>Quantity: {item.quantity}</span>
        //                     </div>
        //                 </div>
        //                 <div className="align-self-center p-4 item-price">
        //                     <span>{formatPrice(item.productPrice)}</span>
        //                 </div>
        //             </div>
        //         ))}
        //     </div>
        //     <div className="card-footer d-flex justify-content-between bg-transparent">
        //         <div className="mt-2">
        //             <span>Order On: {getOrderDate(order.createdAt)}</span>
        //         </div>
        //         <div className="order-total-price mt-2">
        //             <span>Total: {formatPrice(order.totalPrice)}</span>
        //         </div>
        //     </div>
        // </div>
        <div className="border rounded-lg shadow-md p-4 mb-6 bg-white" key={order.id}>
            {/* Header */}
            <div className="flex justify-between items-center border-b pb-2 text-gray-700">
                <span className="font-semibold">Delivery Method: {order.deliveryMethod}</span>
                <span className="font-semibold">Delivery Status: {order.deliveryStatus}</span>
                <span className="font-semibold">Status: {order.orderStatus}</span>
            </div>

            {/* Order Items */}
            <div className="py-4 space-y-4">
                {order.orderItemVms.map((item) => (
                    <div key={item.id} className="flex gap-4 items-center border-b pb-4">
                        <Link href={{pathname: '/redirect', query: {productId: item.productId}}}>
                            <LoadImageSafe
                                src={item.productAvatarUrl}
                                alt={item.productName}
                                className="w-24 h-24 object-cover rounded-lg cursor-pointer"
                            />
                        </Link>
                        <div className="flex-1">
                            <Link
                                href={{pathname: '/redirect', query: {productId: item.productId}}}
                                className="text-blue-600 hover:underline font-medium"
                            >
                                {item.productName}
                            </Link>
                            <div className="text-gray-600 font-semibold">Quantity: {item.quantity}</div>
                        </div>
                        <span className="font-semibold text-gray-800">{formatPrice(item.productPrice)}</span>
                    </div>
                ))}
            </div>

            <div className="flex justify-between items-center text-gray-700 mt-4">
                <span>Order On: {getOrderDate(order.createdAt)}</span>
                <span className="font-semibold text-lg">Total: {formatPrice(order.totalPrice)}</span>
            </div>

        </div>


    );

}