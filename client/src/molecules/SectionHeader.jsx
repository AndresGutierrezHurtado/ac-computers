import SectionLabel from "@/atoms/SectionLabel";
import Link from "next/link";

export default function SectionHeader({ label, title, highlight, linkHref }) {
    return (
        <div className="w-full flex justify-between items-center">
            <div>
                {label && <SectionLabel>{label}</SectionLabel>}
                <h1 className="text-5xl font-extrabold text-balance">
                    {title} {highlight && <span className="text-primary italic">{highlight}</span>}
                </h1>
            </div>
            {linkHref && (
                <Link
                    href={linkHref}
                    className="group text-primary font-medium text-sm flex items-center gap-2 text-nowrap"
                >
                    <p>Ver Todos</p>
                    <span className="text-lg">»</span>
                </Link>
            )}
        </div>
    );
}
