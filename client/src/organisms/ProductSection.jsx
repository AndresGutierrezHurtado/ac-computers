import { forwardRef } from "react";
import SectionHeader from "@/molecules/SectionHeader";
import CatalogButtons from "@/molecules/CatalogButtons";

const ProductSection = forwardRef(function ProductSection(
    { label, title, highlight, description, listHref, listLabel, pdfHref, align = "left" },
    ref,
) {
    return (
        <section className="w-full px-3 snap-center">
            <div className="w-full h-auto lg:h-[90vh] max-w-[1200px] mx-auto flex items-center">
                {align === "right" && <article className="w-3/5 hidden lg:block"></article>}
                <article
                    ref={ref}
                    className={`w-full ${align === "right" ? "lg:w-3/5" : "lg:max-w-3/5"} flex flex-col gap-4`}
                >
                    <SectionHeader
                        label={label}
                        title={title}
                        highlight={highlight}
                        linkHref={listHref}
                    />
                    <p className="text-pretty w-full text-gray-300 text-lg mb-4">{description}</p>
                    <CatalogButtons listHref={listHref} listLabel={listLabel} pdfHref={pdfHref} />
                </article>
            </div>
        </section>
    );
});

export default ProductSection;
