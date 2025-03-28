import {NextPage} from "next";
import {NavigationPathModel} from "@/models/Navigation/NavigationPathModel";
import {Container} from "react-bootstrap";
import NavigationComponent from "@/components/common/navigationComponent";
import {FC} from "react";

type Props  = {
    navigationPaths : NavigationPathModel[],
    menuActive : string,
    title?: string|undefined,
    children : React.ReactNode


}
// có thể tận dụng những thẻ khác nhưng phần chilrenr là mỗi khác
const ProfileLayoutComponent : FC<Props> = ({children,menuActive,title,navigationPaths})=>{
    return (
        <Container>
            <head title={title ?? 'Profile'}></head>
            <div
                className="d-flex justify-content-between pt-5 col-md-12 mb-2"
                style={{height: '100px'}}
            >
                <NavigationComponent props={navigationPaths}></NavigationComponent>
            </div>
            <div className="container mb-5">
                <div className="row">
                    <div className="col-md-3 p-0">

                    </div>
                    <div className="col-md-9">{children}</div>
                </div>
            </div>
        </Container>

    )
}
export default ProfileLayoutComponent;