"use client";

import ProductListTemplate from "@/templates/ProductListTemplate";
import { useState } from "react";

export default function ComponentsPage() {
    const [search, setSearch] = useState("");

    return (
        <ProductListTemplate
            title="Lista componentes:"
            searchPlaceholder="Buscar componentes"
            searchValue={search}
            searchOnChange={setSearch}
        >
            {/* Components will be rendered here */}
        </ProductListTemplate>
    );
}
