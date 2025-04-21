import {FC, use, useState} from "react";
import {Tab} from "react-bootstrap";
import FormPostRating from "@/components/rating/FormPostRating";
import ListRating from "@/components/rating/ListRating";
type Props = {
    totalRating: number,

}
const TabRating : FC<Props> = ({totalRating})=>{
    const [ratingStar,setRatingStar] = useState<number>(5);
    const [contentRating , setContentRating] = useState<string>('');
    const handleChangeRating = ()=>{

    }
    const  handleCreateRating = ()=>{

    }
    return (
        <Tab eventKey="Reviews" title={`Reviews (${totalRating})`} style={{ minHeight: '200px' }}>
            <div>
                <div
                    style={{
                        borderBottom: '1px solid lightgray',
                        marginBottom: 30,
                    }}
                >
                    <FormPostRating
                        ratingStar={ratingStar}

                    />
                </div>
                <div>
                    <ListRating

                    />
                </div>
            </div>
        </Tab>
    )
}
export default TabRating