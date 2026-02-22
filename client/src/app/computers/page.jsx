"use client";

import ProductListTemplate from "@/templates/ProductListTemplate";
import ProductCard from "@/organisms/ProductCard";
import { useState, useMemo } from "react";
import { usePaginateData } from "@/hooks/useClientData";

export default function ComputersPage() {
    const [search, setSearch] = useState("");

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.append("categoryId", "1"); // Computadores es la categoría 1

        if (search) {
            params.append("search", search);
        }

        return `/products?${params.toString()}`;
    }, [search]);

    const { data: products, loading } = usePaginateData(endpoint);

    return (
        <ProductListTemplate
            title="Lista computadores"
            searchPlaceholder="Buscar computadores"
            searchValue={search}
            searchOnChange={setSearch}
        >
            {loading ? (
                <p className="text-gray-400">Cargando computadores...</p>
            ) : products.length > 0 ? (
                products.map((product) => <ProductCard key={product.id} product={product} />)
            ) : (
                <p className="text-gray-400">No se encontraron productos para tu búsqueda.</p>
            )}
        </ProductListTemplate>
    );
}
