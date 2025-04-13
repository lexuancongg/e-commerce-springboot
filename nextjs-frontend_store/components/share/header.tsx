"use client"
import React, {useContext, useEffect, useRef, useState} from "react";
import {data_menu} from "@/asset/data/data_menu";
import Link from "next/link";
import { useCartContext} from "@/context/cartContext";

const  Header  = ({children}:React.PropsWithChildren) =>{
    const {numberCartItems} = useCartContext();
    const [showDropdownSuggestions , setShowDropdownSuggestions] =useState<boolean>(false);
    const [searchInput,setSearchInput] = useState<string>('');
    const inputSearchRef = useRef<HTMLInputElement>(null);
    const formRef = useRef<HTMLFormElement>(null);

    // funcition event
    const handleInputFocus = ()=>{
        setShowDropdownSuggestions(true);
    }

    const handleSubmitSearch = (event: React.FormEvent<HTMLFormElement>)=>{

    }

    useEffect(()=>{
        const handleClickOutside = (event:MouseEvent)=>{
            if(formRef.current && !formRef.current.contains(event.target as Node)){
                setShowDropdownSuggestions(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return ()=>{
            document.removeEventListener('mousedown',handleClickOutside);
        }

    },[])
    return (
        <header>

            <div className="header">
                <nav className="top-bar">
                    <div className="top-bar-container container">
                        <div className="left-top-bar">le xuan cong hello</div>
                        <div className="right-top-bar d-flex h-full">
                            {
                                data_menu.map((item) => (
                                    <Link href={item.links} className="d-flex align-items-center px-4" key={item.id}>
                                        {item.icon && (
                                            <div className="icon-header-bell-question">
                                                <i className={item.icon}></i>
                                            </div>
                                        )}
                                        {item.name}
                                    </Link>
                                ))
                            }
                            <div className="d-flex align-items-center px-4">{children}</div>



                        </div>
                    </div>
                </nav>
                <nav className="limiter-menu-desktop container">
                    {/* <!-- Logo desktop --> */}
                    <Link href="/" className="header-logo me-3">
                        <h3 className="text-black">LE XUAN CONG HÊLO</h3>
                    </Link>

                    {/* <!-- Search --> */}
                    <div className="header-search flex-grow-1">
                        <div className="search-wrapper">
                            <form onSubmit={handleSubmitSearch} className="search-form" ref={formRef}>
                                <label htmlFor="header-search" className="search-icon">
                                    <i className="bi bi-search"></i>
                                </label>
                                <input
                                    id="header-search"
                                    ref={inputSearchRef}
                                    className="search-input"
                                    placeholder="What you will find today?"
                                    onFocus={handleInputFocus}
                                    value={searchInput}
                                    onChange={(e) => setSearchInput(e.target.value)}
                                />

                                {showDropdownSuggestions && (
                                    <div className="search-auto-complete">
                                        <div className="suggestion">

                                        </div>
                                        <div className="bottom-widgets"></div>
                                    </div>
                                )}

                                <button type="submit" className="search-button">
                                    Search
                                </button>
                            </form>
                        </div>

                        <div className="search-suggestion">

                        </div>
                    </div>

                    {/* <!-- Cart --> */}
                    <Link className="header-cart" href="/cart">
                        <div className="icon-cart">
                            <i className="bi bi-cart3"></i>
                        </div>
                        <div className="quantity-cart">{numberCartItems}</div>
                    </Link>
                </nav>
                <nav className="limiter-menu-desktop container"></nav>
            </div>
            {showDropdownSuggestions && <div className="container-layer"></div>}
            <div className="lower-container"></div>
        </header>
    )

}
export default Header;

