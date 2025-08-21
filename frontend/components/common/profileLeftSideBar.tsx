import { FaAddressBook, FaUser } from "react-icons/fa"

type Props = {
    menuActive: string
}

export default function ProfileLeftSideBar({ menuActive }: Props) {
    return (
        <div className="w-64 border rounded-lg shadow-md bg-white">
            <div
                className={`flex items-center gap-3 p-4 cursor-pointer ${menuActive === "profile" ? "bg-gray-100" : ""
                    }`}
              
            >
                <FaUser className="text-gray-600" />
                <span className="text-gray-800">User Profile</span>
            </div>
            <div
                className={`flex items-center gap-3 p-4 cursor-pointer ${menuActive === "address" ? "bg-gray-100" : ""
                    }`}
            >
                <FaAddressBook className="text-gray-600" />
                <span className="text-gray-800">Address</span>
            </div>

        </div>
    )
}