"use client";

import { useEffect, useMemo, useState } from "react";
import { useGetData, usePaginateData } from "@/hooks/useClientData";
import ProductMarketplaceTemplate from "@/templates/ProductMarketplaceTemplate";
import ProductFiltersPanel from "@/organisms/ProductFiltersPanel";
import ProductResultsGrid from "@/organisms/ProductResultsGrid";
import { useSearchParams } from "next/navigation";

const CONDITION_OPTIONS = [
    { value: "new", label: "Nuevo" },
    { value: "used", label: "Usado" },
    { value: "refurbished", label: "Reacondicionado" },
    { value: "for_parts", label: "Para repuestos" },
];

const DEFAULT_FILTERS = {
    search: "",
    categoryId: "",
    subCategoryId: "",
    brandId: "",
    condition: "",
    minPrice: "",
    maxPrice: "",
    minDiscount: "",
    maxDiscount: "",
    page: 1,
    perPage: 12,
};

export default function ProductsMarketplace() {
    const searchParams = useSearchParams();
    const initialCategoryId = searchParams.get("categoryId");

    const [filters, setFilters] = useState(() => ({
        ...DEFAULT_FILTERS,
        categoryId: initialCategoryId || "",
    }));

    const { data: categories } = useGetData("/categories");
    const { data: brands } = useGetData("/brands");

    const subCategoryEndpoint = useMemo(() => {
        if (filters.categoryId) {
            return `/subcategories?categoryId=${filters.categoryId}`;
        }
        return "/subcategories";
    }, [filters.categoryId]);
    const { data: subCategories } = useGetData(subCategoryEndpoint);

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        if (filters.search) params.append("search", filters.search);
        if (filters.categoryId) params.append("categoryId", filters.categoryId);
        if (filters.subCategoryId) params.append("subCategoryId", filters.subCategoryId);
        if (filters.brandId) params.append("brandId", filters.brandId);
        if (filters.condition) params.append("condition", filters.condition);
        if (filters.minPrice) params.append("minPrice", filters.minPrice);
        if (filters.maxPrice) params.append("maxPrice", filters.maxPrice);
        if (filters.minDiscount) params.append("minDiscount", filters.minDiscount);
        if (filters.maxDiscount) params.append("maxDiscount", filters.maxDiscount);
        params.append("page", String(filters.page));
        params.append("perPage", String(filters.perPage));

        return `/products?${params.toString()}`;
    }, [filters]);

    const { data: products, loading, total } = usePaginateData(endpoint);

    const updateFilters = (patch) => {
        setFilters((prev) => ({
            ...prev,
            ...patch,
            page: 1,
        }));
    };

    const updatePaging = (patch) => {
        setFilters((prev) => ({
            ...prev,
            ...patch,
        }));
    };

    const handleReset = () => {
        setFilters(DEFAULT_FILTERS);
    };

    useEffect(() => {
        setFilters({
            ...DEFAULT_FILTERS,
            categoryId: initialCategoryId || "",
            subCategoryId: "",
            page: 1,
        });
    }, [initialCategoryId]);

    return (
        <ProductMarketplaceTemplate
            title="Productos"
            searchValue={filters.search}
            onSearchChange={(value) => updateFilters({ search: value })}
            filters={
                <ProductFiltersPanel
                    filters={filters}
                    categories={categories || []}
                    subCategories={subCategories || []}
                    brands={brands || []}
                    conditions={CONDITION_OPTIONS}
                    onCategoryChange={(value) =>
                        updateFilters({ categoryId: value, subCategoryId: "" })
                    }
                    onSubCategoryChange={(value) => updateFilters({ subCategoryId: value })}
                    onBrandChange={(value) => updateFilters({ brandId: value })}
                    onConditionChange={(value) => updateFilters({ condition: value })}
                    onMinPriceChange={(value) => updateFilters({ minPrice: value })}
                    onMaxPriceChange={(value) => updateFilters({ maxPrice: value })}
                    onMinDiscountChange={(value) => updateFilters({ minDiscount: value })}
                    onMaxDiscountChange={(value) => updateFilters({ maxDiscount: value })}
                    onPerPageChange={(value) =>
                        updatePaging({ perPage: value, page: 1 })
                    }
                    onReset={handleReset}
                />
            }
        >
            <ProductResultsGrid
                products={products || []}
                loading={loading}
                total={total}
                page={filters.page}
                perPage={filters.perPage}
                onPageChange={(nextPage) =>
                    updatePaging({ page: Math.max(1, nextPage) })
                }
            />
        </ProductMarketplaceTemplate>
    );
}
