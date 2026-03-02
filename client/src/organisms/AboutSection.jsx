import { forwardRef } from "react";
import SectionLabel from "@/atoms/SectionLabel";
import StatCard from "@/atoms/StatCard";

const STATS = [
    { title: "+10 años", text: "Vendiendo los mejores productos tecnologicos" },
    { title: "+100 clientes", text: "Satisfechos con nuestros productos y servicios" },
    { title: "+150 ventas", text: "En los ultimos 5 años" },
    { title: "100% seguro", text: "Nuestros productos y servicios son seguros y confiables" },
];

const AboutSection = forwardRef(function AboutSection(_, ref) {
    return (
        <section className="w-full px-3 snap-center">
            <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                <article ref={ref} className="w-full lg:max-w-3/5 flex flex-col gap-5">
                    <div>
                        <SectionLabel>Sobre nosotros</SectionLabel>
                        <h1 className="text-5xl font-extrabold">Conocenos más</h1>
                    </div>
                    <p className="text-pretty w-full text-gray-300 text-lg mb-5">
                        En AC Computers nos enfocamos en brindar a nuestros clientes los mejores
                        productos y servicios al mejor precio del mercado. A continuacion, te
                        presentamos algunas de las caracteristicas que nos hacen destacar.
                    </p>
                    <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-10">
                        {STATS.map((item, index) => (
                            <StatCard key={index} title={item.title} text={item.text} />
                        ))}
                    </div>
                </article>
                <article className="w-2/5 hidden lg:block"></article>
            </div>
        </section>
    );
});

export default AboutSection;
