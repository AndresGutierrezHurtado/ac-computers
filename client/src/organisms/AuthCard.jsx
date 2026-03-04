import Link from "next/link";

export default function AuthCard({ title, subtitle, brandHref, children }) {
    const brand = (
        <h1 className="text-4xl font-extrabold text-center text-primary">{title}</h1>
    );

    return (
        <div className="card bg-base-100 w-full max-w-[500px] shrink-0 shadow-2xl">
            <div className="card-body flex flex-col gap-2 p-10 px-7">
                <div>
                    {brandHref ? (
                        <Link href={brandHref} aria-label="Ir al inicio">
                            {brand}
                        </Link>
                    ) : (
                        brand
                    )}
                    <p className="text-center text-2xl font-medium">{subtitle}</p>
                </div>
                {children}
            </div>
        </div>
    );
}
