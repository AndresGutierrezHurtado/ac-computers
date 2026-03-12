import ProductCard from "@/organisms/ProductCard";
import { BackIcon } from "@/atoms/Icons";

export default function ProductResultsGrid({
    products = [],
    loading,
    total = 0,
    page,
    perPage,
    onPageChange,
}) {
    const totalPages = perPage ? Math.ceil(total / perPage) : 1;
    const canGoPrev = page > 1;
    const canGoNext = page < totalPages;

    return (
        <div className="space-y-4">
            <div className="flex items-center justify-between">
                <p className="text-sm text-base-content/70">
                    Resultados: {total || products.length}
                </p>
                <div className="flex items-center gap-2">
                    <button
                        type="button"
                        className="btn btn-sm btn-outline"
                        onClick={() => onPageChange(page - 1)}
                        disabled={!canGoPrev}
                        aria-label="Pagina anterior"
                    >
                        <BackIcon />
                    </button>
                    <span className="text-sm">
                        {page} / {totalPages || 1}
                    </span>
                    <button
                        type="button"
                        className="btn btn-sm btn-outline"
                        onClick={() => onPageChange(page + 1)}
                        disabled={!canGoNext}
                        aria-label="Pagina siguiente"
                    >
                        <BackIcon className="rotate-180" />
                    </button>
                </div>
            </div>

            {loading ? (
                <p className="text-gray-400">Cargando productos...</p>
            ) : products.length > 0 ? (
                <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-8">
                    {products.map((product) => (
                        <ProductCard key={product.id} product={product} />
                    ))}
                </div>
            ) : (
                <p className="text-gray-400">No se encontraron productos.</p>
            )}
        </div>
    );
}
