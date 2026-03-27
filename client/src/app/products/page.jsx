"use client";

import ProductsMarketplace from "@/organisms/ProductsMarketplace";
import AIChatWidget from "@/organisms/AIChatWidget";

import { usePageTitle } from "@/hooks/usePageTitle";

export default function ProductsPage() {
    usePageTitle("Productos");

    return (
        <>
            <ProductsMarketplace />
            <AIChatWidget />
        </>
    );
}
