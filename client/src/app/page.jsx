"use client";
import HomeTemplate from "@/templates/HomeTemplate";
import HeroSection from "@/organisms/HeroSection";
import ProductSection from "@/organisms/ProductSection";
import AboutSection from "@/organisms/AboutSection";

export default function Home() {
    return (
        <HomeTemplate>
            <HeroSection />
            <ProductSection
                label="Los mejores"
                title="Componentes"
                highlight="para tu PC"
                description="Descubre nuestra amplia selección de componentes de la más alta calidad, que abarca desde avanzadas placas base hasta veloces discos duros, potentes tarjetas gráficas y memorias RAM de gran capacidad."
                listHref="/components"
                listLabel="Ver lista de componentes"
                pdfHref="/api/products/pdf?type=2"
                align="right"
            />
            <ProductSection
                label="Los mejores"
                title="Computadores"
                description="Descubre nuestra amplia selección de computadores de la más alta calidad, que abarca desde potentes laptops hasta veloces desktops, pasando por avanzadas workstations y servidores."
                listHref="/computers"
                listLabel="Ver lista de computadores"
                pdfHref="/api/products/pdf?type=1"
                align="left"
            />
            <AboutSection />
        </HomeTemplate>
    );
}
