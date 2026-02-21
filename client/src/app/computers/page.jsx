"use client";

import ProductListTemplate from "@/templates/ProductListTemplate";
import { useState } from "react";

export default function ComputersPage() {
    const [search, setSearch] = useState("");

    return (
        <ProductListTemplate
            title="Lista computadores:"
            searchPlaceholder="Buscar computadores"
            searchValue={search}
            searchOnChange={setSearch}
        >
            {/* Products will be rendered here */}
        </ProductListTemplate>
    );
}
