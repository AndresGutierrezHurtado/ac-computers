import SearchInput from "@/atoms/SearchInput";
import { PriceTagsIcon } from "@/atoms/icons";

export default function ProductMarketplaceTemplate({
    title,
    filters,
    children,
    searchValue,
    onSearchChange,
}) {
    return (
        <main className="w-full">
            <div className="drawer">
                <input id="products-filters-drawer" type="checkbox" className="drawer-toggle" />
                <div className="drawer-content">
                    <section className="w-full px-3 mt-[100px]">
                        <div className="w-full max-w-[1200px] mx-auto space-y-6">
                            <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
                                <h1 className="text-3xl font-bold">{title}</h1>
                                <div className="flex flex-col sm:flex-row sm:items-center gap-3">
                                    <SearchInput
                                        placeholder="Buscar productos"
                                        value={searchValue}
                                        onChange={onSearchChange}
                                    />
                                    <label
                                        htmlFor="products-filters-drawer"
                                        className="btn btn-outline btn-sm"
                                    >
                                        <PriceTagsIcon />
                                        Filtros
                                    </label>
                                </div>
                            </div>
                            <div className="w-full">{children}</div>
                        </div>
                    </section>
                </div>
                <div className="drawer-side z-[999]">
                    <label
                        htmlFor="products-filters-drawer"
                        className="drawer-overlay z-[999]"
                    ></label>
                    <aside className="w-96 min-h-full bg-base-200 p-4 z-[999]">
                        {filters}
                    </aside>
                </div>
            </div>
        </main>
    );
}
