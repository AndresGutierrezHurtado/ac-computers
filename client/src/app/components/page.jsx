"use client";

import ProductListTemplate from "@/templates/ProductListTemplate";
import { useState, useMemo } from "react";
import { usePaginateData } from "@/hooks/useClientData";
import ProductCard from "@/organisms/ProductCard";

export default function ComponentsPage() {
    const [search, setSearch] = useState("");

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.append("categoryId", "2"); // Componentes es la categoría 2

        if (search) {
            params.append("search", search);
        }

        return `/products?${params.toString()}`;
    }, [search]);

    const { data: paginationData, loading } = usePaginateData(endpoint);
    const components = paginationData?.data || [];

    return (
        <ProductListTemplate
            title="Lista componentes:"
            searchPlaceholder="Buscar componentes"
            searchValue={search}
            searchOnChange={setSearch}
        >
            {loading ? (
                <p className="text-gray-400">Cargando componentes...</p>
            ) : components.length > 0 ? (
                components.map((component) => (
                    <ProductCard key={component.id} product={component} />
                ))
            ) : (
                <p className="text-gray-400">No se encontraron componentes para tu búsqueda.</p>
            )}
        </ProductListTemplate>
    );
}
