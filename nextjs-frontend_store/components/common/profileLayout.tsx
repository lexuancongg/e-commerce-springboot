'use client';

import { FC } from "react";
import { Container } from "react-bootstrap";
import { NavigationPathModel } from "@/models/Navigation/NavigationPathModel";
import NavigationComponent from "@/components/common/navigationComponent";
import ProfileLeftSideBar from "./profileLeftSideBar";
import { BiPlusMedical } from "react-icons/bi";
import { HiCheckCircle } from "react-icons/hi";
import { FiEdit } from "react-icons/fi";
import { FaTrash } from "react-icons/fa";
import { TiContacts } from "react-icons/ti";
import Link from "next/link";

// Props type
type Props = {
  navigationPaths: NavigationPathModel[];
  menuActive: string;
  title?: string | undefined;
  children: React.ReactNode;
};

const ProfileLayoutComponent: FC<Props> = ({ children, menuActive, title, navigationPaths }) => {
  return (
    <Container>
      <head title={title ?? "Profile"}></head>
      <div className="flex justify-between pt-5 mb-2 h-24">
        <NavigationComponent props={navigationPaths} />
      </div>
      <div className="container mb-5">
        <div className="grid grid-cols-12 gap-4">
          <div className="col-span-3">
            <ProfileLeftSideBar menuActive={menuActive} />
          </div>
          <div className="col-span-9">{children}</div>
        </div>
      </div>
    </Container>
  );
};

export default ProfileLayoutComponent;