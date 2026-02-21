import Header from "@/organisms/Header";
import Footer from "@/molecules/Footer";

import "./globals.css";

export default function RootLayout({ children }) {
    return (
        <html lang="es">
            <body>
                <div className="flex flex-col min-h-screen gap-10 z-10">
                    <Header />
                    <main className="flex-1">{children}</main>
                    <Footer />
                </div>
            </body>
        </html>
    );
}
