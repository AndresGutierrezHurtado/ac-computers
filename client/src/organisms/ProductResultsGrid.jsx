import ProductCard from "@/organisms/ProductCard";

export default function ProductResultsGrid({
    products = [],
    loading,
    total = 0,
}) {
    return (
        <div className="space-y-4">
            <p className="text-sm text-base-content/70">
                Resultados: {total || products.length}
            </p>

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
