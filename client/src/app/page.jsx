"use client";
import HomeTemplate from "@/templates/HomeTemplate";
import HeroSection from "@/organisms/HeroSection";
import ProductSection from "@/organisms/ProductSection";
import AboutSection from "@/organisms/AboutSection";
import AIChatWidget from "@/organisms/AIChatWidget";

export default function Home() {
    return (
        <>
            <HomeTemplate>
                <HeroSection />
                <ProductSection
                    label="Los mejores"
                    title="Componentes"
                    highlight="para tu PC"
                    description="Descubre nuestra amplia selección de componentes de la más alta calidad, que abarca desde avanzadas placas base hasta veloces discos duros, potentes tarjetas gráficas y memorias RAM de gran capacidad."
                    listHref="/products?categoryId=2"
                    listLabel="Ver lista de componentes"
                    pdfEndpoint="/pdf/generate?type=2"
                    align="right"
                />
                <ProductSection
                    label="Los mejores"
                    title="Computadores"
                    description="Descubre nuestra amplia selección de computadores de la más alta calidad, que abarca desde potentes laptops hasta veloces desktops, pasando por avanzadas workstations y servidores."
                    listHref="/products?categoryId=1"
                    listLabel="Ver lista de computadores"
                    pdfEndpoint="/pdf/generate?type=1"
                    align="left"
                />
                <ProductSection
                    label="Los mejores"
                    title="Periféricos"
                    description="Encuentra monitores, teclados, mouse y accesorios que elevan tu experiencia, con opciones para trabajo y gaming."
                    listHref="/products?categoryId=3"
                    listLabel="Ver lista de periféricos"
                    pdfEndpoint="/pdf/generate?categoryId=3"
                    align="right"
                />
                <AboutSection />
            </HomeTemplate>
            <AIChatWidget />
        </>
    );
}
