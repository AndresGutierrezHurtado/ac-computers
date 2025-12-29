"use client";
import { DownloadIcon, GearIcon, UsersIcon } from "@/components/icons";
import Modelo3D from "@/components/model";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/all";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";

gsap.registerPlugin(ScrollTrigger);

export default function Home() {
    const mainref = useRef();
    const sceneRef = useRef();
    const sectionRefs = useRef([]);

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        document.title = "Inicio | AC Computers";

        gsap.set(sectionRefs.current[1], { x: "150%", opacity: "-3.5" });
        gsap.set(sectionRefs.current[2], { x: "-150%", opacity: "-3.5" });
        gsap.set(sectionRefs.current[3], { x: "150%", opacity: "-3.5" });

        gsap.timeline({
            scrollTrigger: {
                trigger: mainref.current,
                start: "top top",
                end: "bottom bottom",
                scrub: 1,
                onUpdate: (self) => {
                    setProgress(self.progress);
                },
            },
        })
            .to(sceneRef.current, {
                x: "-43%",
                opacity: 1,
                ease: "none",
            })
            .to(sceneRef.current, {
                x: "3%",
                ease: "none",
            })
            .to(sceneRef.current, {
                x: "-43%",
                ease: "none",
            });

        gsap.timeline({
            scrollTrigger: {
                trigger: mainref.current,
                start: "top top",
                end: "bottom bottom",
                scrub: 1,
                onUpdate: (self) => {
                    setProgress(self.progress);
                },
            },
        })
            .to(sectionRefs.current[1], {
                x: "0%",
                opacity: 1,
                ease: "none",
            })
            .to(sectionRefs.current[2], {
                x: "0%",
                opacity: 1,
                ease: "none",
            })
            .to(sectionRefs.current[3], {
                x: "0%",
                opacity: 1,
                ease: "none",
            });
    }, []);

    return (
        <main
            className="w-full min-h-screen flex flex-col gap-[100px] lg:gap-10 overflow-x-hidden overflow-y-scroll snap-y snap-mandatory"
            ref={mainref}
        >
            <div
                className="fixed top-[2.5%] right-[-20%] h-screen w-screen hidden lg:block pointer-events-none z-40 snap-center"
                ref={sceneRef}
            >
                <Modelo3D progress={progress} />
            </div>

            {/* Hero section */}
            <section className="w-full px-3 mt-[150px] lg:mt-0 snap-center">
                <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                    <article
                        ref={(el) => (sectionRefs.current[0] = el)}
                        className="w-full lg:w-1/2 flex flex-col gap-2"
                    >
                        <div className="badge bg-primary/20 text text-primary font-medium border border-primary/50 mb-1">
                            <GearIcon size={12} /> Lo mejor de la tecnologia
                        </div>

                        <h1 className="text-5xl md:text-6xl font-extrabold">
                            AC <span className="text-primary italic">COMPUTERS</span>
                        </h1>
                        <p className="text-xl mb-6">
                            Donde la <span className="text-primary">tecnologia</span> te acompaña.
                        </p>
                        <p className="text-pretty w-full text-gray-300 text-lg mb-8">
                            Creemos que la tecnologia es el futuro y no tiene porque costar una
                            fortuna. Descubre nuestra collecion de computadoras y componentes a
                            precios que se adaptan a tu estilo de vida.
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

            {/* Second section */}
            <section className="w-full px-3 snap-center">
                <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                    <article className="w-3/5 hidden lg:block"></article>
                    <article
                        ref={(el) => (sectionRefs.current[1] = el)}
                        className="w-full lg:w-3/5 flex flex-col gap-4"
                    >
                        <div className="w-full flex justify-between items-center">
                            <div>
                                <p className="text-primary font-bold tracking-tight uppercase">
                                    Los mejores
                                </p>
                                <h1 className="text-5xl font-extrabold text-balance">
                                    Componentes{" "}
                                    <span className="text-primary italic">para tu PC</span>
                                </h1>
                            </div>
                            <Link
                                href="/components"
                                className="group text-primary font-medium text-sm flex items-center gap-2 text-nowrap"
                            >
                                <p>Ver Todos</p>
                                <span className="text-lg">»</span>
                            </Link>
                        </div>
                        <p className="text-pretty w-full text-gray-300 text-lg mb-4">
                            Descubre nuestra amplia selección de componentes de la más alta calidad,
                            que abarca desde avanzadas placas base hasta veloces discos duros,
                            potentes tarjetas gráficas y memorias RAM de gran capacidad.
                        </p>
                        <div className="flex gap-4">
                            <Link href="/components">
                                <button className="btn btn-outline border-primary text-primary hover:bg-primary/10 w-fit rounded-lg font-medium">
                                    Ver lista de componentes
                                </button>
                            </Link>
                            <Link href="/api/products/pdf?type=2" target="_blank">
                                <button className="btn bg-primary hover:bg-primary/80 w-fit text-black rounded-lg font-medium">
                                    <DownloadIcon size={18} />
                                    Descargar Catalogo Componentes
                                </button>
                            </Link>
                        </div>
                    </article>
                </div>
            </section>

            {/* Third section */}
            <section className="w-full px-3 snap-center">
                <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                    <article
                        ref={(el) => (sectionRefs.current[2] = el)}
                        className="w-full lg:max-w-3/5 flex flex-col gap-5"
                    >
                        <div className="w-full flex justify-between items-center">
                            <div>
                                <p className="text-primary font-bold tracking-tight uppercase">
                                    Los mejores
                                </p>
                                <h1 className="text-5xl font-extrabold">Computadores</h1>
                            </div>
                            <Link
                                href="/computers"
                                className="group text-primary font-medium text-sm flex items-center gap-2 text-nowrap"
                            >
                                <p>Ver Todos</p>
                                <span className="text-lg">»</span>
                            </Link>
                        </div>
                        <p className="text-pretty w-full text-gray-300 text-lg mb-4">
                            Descubre nuestra amplia selección de computadores de la más alta
                            calidad, que abarca desde potentes laptops hasta veloces desktops,
                            pasando por avanzadas workstations y servidores.
                        </p>
                        <div className="flex gap-4">
                            <Link href="/computers">
                                <button className="btn btn-outline border-primary text-primary hover:bg-primary/10 w-fit rounded-lg font-medium">
                                    Ver lista de computadores
                                </button>
                            </Link>
                            <Link href="/api/products/pdf?type=1" target="_blank">
                                <button className="btn bg-primary hover:bg-primary/80 w-fit text-black rounded-lg font-medium">
                                    <DownloadIcon size={18} />
                                    Descargar Catalogo Computadores
                                </button>
                            </Link>
                        </div>
                    </article>
                </div>
            </section>

            {/* Fourth section */}
            <section className="w-full px-3 snap-center">
                <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                    <article className="w-2/5 hidden lg:block"></article>
                    <article
                        ref={(el) => (sectionRefs.current[3] = el)}
                        className="w-full lg:max-w-3/5 flex flex-col gap-5"
                    >
                        <div>
                            <p className="text-primary font-bold tracking-tight uppercase">
                                Sobre nosotros
                            </p>
                            <h1 className="text-5xl font-extrabold">Conocenos más</h1>
                        </div>
                        <p className="text-pretty w-full text-gray-300 text-lg mb-5">
                            En AC Computers nos enfocamos en brindar a nuestros clientes los mejores
                            productos y servicios al mejor precio del mercado. A continuacion, te
                            presentamos algunas de las caracteristicas que nos hacen destacar.
                        </p>
                        <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-10">
                            {[
                                {
                                    title: "+10 años",
                                    text: "Vendiendo los mejores productos tecnologicos",
                                },
                                {
                                    title: "+100 clientes",
                                    text: "Satisfechos con nuestros productos y servicios",
                                },
                                {
                                    title: "+150 ventas",
                                    text: "En los ultimos 5 años",
                                },
                                {
                                    title: "100% seguro",
                                    text: "Nuestros productos y servicios son seguros y confiables",
                                },
                            ].map((item, index) => (
                                <div
                                    key={index}
                                    className="flex flex-col items-start gap-2 text-center border-t-2 border-primary py-2 w-full max-w-[300px] mx-auto"
                                >
                                    <h3 className="text-2xl font-bold uppercase text-primary w-full">
                                        {item.title}
                                    </h3>
                                    <p className="font-medium text-sm text-gray-300 w-full">
                                        {item.text}
                                    </p>
                                </div>
                            ))}
                        </div>
                    </article>
                </div>
            </section>
        </main>
    );
}
