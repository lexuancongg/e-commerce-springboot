import ProfileLayoutComponent from "@/components/common/profileLayout";
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";

const navigationPaths : NavigationPathModel[] = [
    {
        pageName:"Home",
        url:'#'
    },
    {
        pageName:'Address',
        url:'#'
    },
    {
        pageName:'Create',
        url:'#'
    }
]
export function CreateAddress (){
    return (
        <ProfileLayoutComponent menuActive="address" navigationPaths={navigationPaths}>
            <p></p>
        </ProfileLayoutComponent>
    )
}
