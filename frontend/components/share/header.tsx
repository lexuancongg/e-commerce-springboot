"use client"
import React, { useContext, useEffect, useRef, useState } from "react";
import { data_menu } from "@/asset/data/data_menu";
import Link from "next/link";
import { useCartContext } from "@/context/cartContext";
import LoadImageSafe from "../common/loadImageSafe";
import { Dropdown } from "react-bootstrap";

const Header = ({ children }: React.PropsWithChildren) => {
    const { numberCartItems } = useCartContext();
    const [showDropdownSuggestions, setShowDropdownSuggestions] = useState<boolean>(false);
    const [searchInput, setSearchInput] = useState<string>('');
    const inputSearchRef = useRef<HTMLInputElement>(null);
    const formRef = useRef<HTMLFormElement>(null);

    // funcition event
    const handleInputFocus = () => {
        setShowDropdownSuggestions(true);
    }

    const handleSubmitSearch = (event: React.FormEvent<HTMLFormElement>) => {

    }

    const categories = [
        { id: 1, name: 'Electronics', slug: 'electronics' },
        { id: 2, name: 'Mobile Phones', slug: 'mobile-phones' },
        { id: 3, name: 'Laptops', slug: 'laptops' },
        { id: 4, name: 'Cameras', slug: 'cameras' },
        { id: 5, name: 'Audio', slug: 'audio' },
        { id: 6, name: 'Smart Watches', slug: 'smart-watches' },
        { id: 7, name: 'Accessories', slug: 'accessories' },
        { id: 8, name: 'Home Appliances', slug: 'home-appliances' },
        { id: 9, name: 'Gaming', slug: 'gaming' },
    ];


    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (formRef.current && !formRef.current.contains(event.target as Node)) {
                setShowDropdownSuggestions(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        }

    }, [])
    // return (
    //     <header>
    //         <div className="header">
    //             <nav className="top-bar">
    //                 <div className="top-bar-container container">
    //                     <div className="left-top-bar">
    //                         <div>SHOP XUANCONG</div>
    //                     </div>


    //                     <div className="right-top-bar d-flex h-full">
    //                         {
    //                             data_menu.map((item) => (
    //                                 <Link href={item.links} className="d-flex align-items-center px-4" key={item.id}>
    //                                     {item.icon && (
    //                                         <div className="icon-header-bell-question">
    //                                             <i className={item.icon}></i>
    //                                         </div>
    //                                     )}
    //                                     {item.name}
    //                                 </Link>
    //                             ))
    //                         }
    //                         <div className="d-flex align-items-center px-4">{children}</div>



    //                     </div>
    //                 </div>
    //             </nav>
    //             <nav className="limiter-menu-desktop container">

    //                 <Link href="/" className="header-logo me-3">
    //                 <LoadImageSafe
    //                  src="https://img.freepik.com/premium-vector/ecommerce-logo-design_624194-152.jpg?w=2000"
    //                   className="h-20 w-20 object-contain rounded-full"
    //                  >

    //                  </LoadImageSafe>
    //                     <h3 className="text-black">SHOP XUANCONG</h3>
    //                 </Link>

    //                 <div className="header-search flex-grow-1">
    //                     <div className="search-wrapper">
    //                         <form onSubmit={handleSubmitSearch} className="search-form" ref={formRef}>
    //                             <label htmlFor="header-search" className="search-icon">
    //                                 <i className="bi bi-search"></i>
    //                             </label>
    //                             <input
    //                                 id="header-search"
    //                                 ref={inputSearchRef}
    //                                 className="search-input"
    //                                 placeholder="What you will find today?"
    //                                 onFocus={handleInputFocus}
    //                                 value={searchInput}
    //                                 onChange={(e) => setSearchInput(e.target.value)}
    //                             />

    //                             {showDropdownSuggestions && (
    //                                 <div className="search-auto-complete">
    //                                     <div className="suggestion">

    //                                     </div>
    //                                     <div className="bottom-widgets"></div>
    //                                 </div>
    //                             )}

    //                             <button type="submit" className="search-button">
    //                                 Search
    //                             </button>
    //                         </form>
    //                     </div>

    //                     <div className="search-suggestion">

    //                     </div>
    //                 </div>


    //                 <Link className="header-cart" href="/cart">
    //                     <div className="icon-cart">
    //                         <i className="bi bi-cart3"></i>
    //                     </div>
    //                     <div className="quantity-cart">{numberCartItems}</div>
    //                 </Link>
    //             </nav>
    //             <nav className="limiter-menu-desktop container"></nav>
    //         </div>
    //         {showDropdownSuggestions && <div className="container-layer"></div>}
    //         <div className="lower-container"></div>
    //     </header>
    // )



    const [showSearch, setShowSearch] = useState(false);

    return (
        <header className="shadow-md bg-white relative">
            {/* ---- Top bar ---- */}
            <nav className="bg-black border-b border-gray-800">
                <div className="container mx-auto flex justify-between items-center h-10 px-4 text-sm">
                    {/* Left */}
                    <div className="text-gray-200 font-medium flex items-center">
                        shop xuancong

                    </div>

                    {/* Right */}
                    <div className="flex items-center gap-6">
                        {data_menu.map((item) => (
                            <Link
                                href={item.links}
                                key={item.id}
                                className="flex items-center gap-1 text-gray-300 hover:text-blue-400 transition"
                            >
                                {item.icon && <i className={item.icon}></i>}
                                {item.name}
                            </Link>
                        ))}
                    </div>
                </div>
            </nav>



            {/* ---- Main header ---- */}
            <div className="container mx-auto flex items-center justify-between py-4">
                {/* Logo */}
                <Link href="/" className="flex items-center gap-2">
                    <img
                        src="https://img.freepik.com/premium-vector/ecommerce-logo-design_624194-152.jpg?w=2000"
                        alt="Logo"
                        className="h-12 w-12 object-contain rounded-full"
                    />
                    <div className="flex flex-col leading-none">
                        <span
                            className="text-sm font-semibold text-gray-500 tracking-wide"
                            style={{ fontFamily: "'Poppins', sans-serif" }}
                        >
                            SHOP
                        </span>
                        <span
                            className="text-xl font-medium text-gray-800 tracking-tight"
                            style={{ fontFamily: "'Poppins', sans-serif" }}
                        >
                            XUANCONG
                        </span>
                    </div>

                </Link>


                <nav className="flex gap-3 items-center text-gray-700 font-medium">
                    {categories.slice(0, 5).map((cat) => (
                        <Link
                            key={cat.id}
                            href={`/category/${cat.slug}`}
                            className="text-gray-700 font-semibold px-3 py-1 rounded-full hover:text-white hover:bg-blue-500 transition-all duration-300"
                            style={{ fontFamily: 'Poppins, sans-serif' }}
                        >
                            {cat.name}
                        </Link>

                    ))}

                    {categories.length > 5 && (
                        <Dropdown>
                            <Dropdown.Toggle className="bg-transparent border-0 text-gray-700 hover:text-blue-500 font-semibold">
                                More
                            </Dropdown.Toggle>
                            <Dropdown.Menu className="bg-white border rounded shadow-lg">
                                {categories.slice(5).map((cat) => (
                                    <Dropdown.Item key={cat.id} href={`/category/${cat.slug}`}>
                                        {cat.name}
                                    </Dropdown.Item>
                                ))}
                            </Dropdown.Menu>
                        </Dropdown>
                    )}

                    <Dropdown>
                        <Dropdown.Toggle className="bg-transparent border-0 text-gray-700 hover:text-blue-500 font-semibold">
                            Pages
                        </Dropdown.Toggle>
                        <Dropdown.Menu className="bg-white border rounded shadow-lg">
                            <Dropdown.Item href="/about">About</Dropdown.Item>
                            <Dropdown.Item href="/contact">Contact</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </nav>


                {/* Icons (Search + Cart) */}
                <div className="flex items-center gap-6">
                    {/* Search icon */}
                    <button
                        onClick={() => setShowSearch(!showSearch)}
                        className="text-xl hover:text-blue-600 transition"
                    >
                        <i className="bi bi-search"></i>
                    </button>

                    {/* Cart */}
                    <Link href="/cart" className="relative">
                        <i className="bi bi-cart3 text-2xl"></i>
                        {numberCartItems > 0 && (
                            <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs px-1.5 py-0.5 rounded-full">
                                {numberCartItems}
                            </span>
                        )}
                    </Link>
                    <div className="flex items-center gap-2">
                        {children}
                    </div>

                </div>
            </div>

            {/* ---- Dropdown search ---- */}
            {showSearch && (
                <div className="absolute left-0 top-full w-full bg-white shadow-md border-t">
                    <div className="container mx-auto py-4">
                        <form className="flex items-center gap-2">
                            <i className="bi bi-search text-gray-500 text-xl"></i>
                            <input
                                type="text"
                                placeholder="Search for products..."
                                className="flex-grow border-b border-gray-300 focus:outline-none focus:border-blue-500 py-1"
                            />
                            <button className="px-4 py-1 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Search
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </header>
    );

}
export default Header;

