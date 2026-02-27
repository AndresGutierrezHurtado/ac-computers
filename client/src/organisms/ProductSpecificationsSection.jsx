export default function ProductSpecificationsSection({ specifications = [] }) {
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto py-10">
                <div className="space-y-2">
                    <h2 className="text-2xl font-extrabold tracking-tight">
                        Especificaciones del producto:
                    </h2>
                    {specifications.length > 0 ? (
                        <ul className="divide-y divide-base-content/10">
                            {specifications.map((spec, index) => {
                                const key =
                                    spec.id ||
                                    spec.specification?.id ||
                                    spec.specification?.slug ||
                                    index;
                                const unit = spec.specification?.unit;
                                return (
                                    <li
                                        key={key}
                                        className="py-3 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-1"
                                    >
                                        <span className="font-semibold">
                                            {spec.specification?.name || "Especificacion"}
                                        </span>
                                        <span className="text-sm text-base-content/70">
                                            {spec.value || "—"}
                                            {unit ? ` ${unit}` : ""}
                                        </span>
                                    </li>
                                );
                            })}
                        </ul>
                    ) : (
                        <p className="text-base-content/60">Sin especificaciones.</p>
                    )}
                </div>
            </div>
        </section>
    );
}
