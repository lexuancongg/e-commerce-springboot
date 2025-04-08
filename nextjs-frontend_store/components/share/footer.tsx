const Footer = () => {
    return (
        <footer className="bg-gray-900 text-gray-300 pt-10 pb-6 px-4 mt-10">
            <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
                {/* About Us */}
                <div>
                    <h4 className="text-lg font-semibold mb-4 text-white">About Us</h4>
                    <p className="text-sm leading-relaxed">
                        We provide the best quality products to ensure your satisfaction.
                        Contact us for more information.
                    </p>
                </div>

                {/* Quick Links */}
                <div>
                    <h4 className="text-lg font-semibold mb-4 text-white">Quick Links</h4>
                    <ul className="space-y-2 text-sm">
                        <li>
                            <a href="/about" className="hover:text-white transition-colors duration-200">About</a>
                        </li>
                        <li>
                            <a href="/contact" className="hover:text-white transition-colors duration-200">Contact</a>
                        </li>
                        <li>
                            <a href="/faq" className="hover:text-white transition-colors duration-200">FAQ</a>
                        </li>
                    </ul>
                </div>

                {/* Contact */}
                <div>
                    <h4 className="text-lg font-semibold mb-4 text-white">Contact</h4>
                    <p className="text-sm">Email: <a href="mailto:support@example.com" className="hover:text-white">support@example.com</a></p>
                    <p className="text-sm">Phone: +1 234 567 890</p>
                </div>
            </div>

            {/* Footer Bottom */}
            <div className="border-t border-gray-700 mt-10 pt-4 text-center text-sm text-gray-500">
                &copy; {new Date().getFullYear()} YourCompany. All rights reserved.
            </div>
        </footer>
    );
};

export default Footer;
