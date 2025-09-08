'use client';

import { FC } from "react";

import ProfileLeftSideBar from "./profileLeftSideBar";


// Props type
type Props = {
  menuActive: string;
  children: React.ReactNode;
};


// ProfileLayoutComponent
const ProfileLayoutComponent: FC<Props> = ({ children, menuActive }) => {
  return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
            <div className="lg:col-span-3">
              <ProfileLeftSideBar menuActive={menuActive} />
            </div>
            <div className="lg:col-span-9">
              {children}
            </div>
          </div>
        </div>
      </div>
  );
};
export default ProfileLayoutComponent;