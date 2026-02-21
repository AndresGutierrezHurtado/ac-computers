import { forwardRef } from "react";
import Badge from "@/atoms/Badge";
import { DownloadIcon, GearIcon, UsersIcon } from "@/atoms/icons";
import Link from "next/link";

const HeroSection = forwardRef(function HeroSection(_, ref) {
    return (
        <section className="w-full px-3 mt-[150px] lg:mt-0 snap-center">
            <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                <article ref={ref} className="w-full lg:w-1/2 flex flex-col gap-2">
                    <Badge>
                        <GearIcon size={12} /> Lo mejor de la tecnologia
                    </Badge>

                    <h1 className="text-5xl md:text-6xl font-extrabold">
                        AC <span className="text-primary italic">COMPUTERS</span>
                    </h1>
                    <p className="text-xl mb-6">
                        Donde la <span className="text-primary">tecnologia</span> te acompaña.
                    </p>
                    <p className="text-pretty w-full text-gray-300 text-lg mb-8">
                        Creemos que la tecnologia es el futuro y no tiene porque costar una fortuna.
                        Descubre nuestra collecion de computadoras y componentes a precios que se
                        adaptan a tu estilo de vida.
                    </p>
                    <Link href="/api/products/pdf" target="_blank">
                        <button className="btn bg-primary hover:bg-primary/80 w-fit text-black rounded-lg mb-3 font-medium">
                            <DownloadIcon size={18} />
                            Descargar Catalogo
                        </button>
                    </Link>
                    <p className="flex items-center gap-1 font-medium text-gray-300">
                        <span className="text-primary flex items-center">
                            + <UsersIcon size={20} className="mr-2" /> 100
                        </span>{" "}
                        clientes satisfechos
                    </p>
                </article>
            </div>
        </section>
    );
});

export default HeroSection;
