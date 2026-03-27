"use client";

import { useParams } from "next/navigation";

import ProductDetailHero from "@/organisms/ProductDetailHero";
import ProductSpecificationsSection from "@/organisms/ProductSpecificationsSection";
import AIChatWidget from "@/organisms/AIChatWidget";
import ProductDetailTemplate from "@/templates/ProductDetailTemplate";

import { useGetData, useStreamData } from "@/hooks/useClientData";
import { usePageTitle } from "@/hooks/usePageTitle";

export default function ProductDetailPage() {
    const params = useParams();
    const productId = Array.isArray(params?.id) ? params.id[0] : params?.id;
    const { data: product, loading } = useGetData(`/products/${productId}`);

    const { text: aiOverview, status: aiOverviewStatus } = useStreamData(`/products/${productId}/overview`);

    usePageTitle(product?.name || "Producto");

    if (loading) {
        return (
            <ProductDetailTemplate>
                <section className="w-full px-3">
                    <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                        <p className="text-base-content/60">Cargando producto...</p>
                    </div>
                </section>
            </ProductDetailTemplate>
        );
    }

    if (!product) {
        return (
            <ProductDetailTemplate>
                <section className="w-full px-3">
                    <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                        <p className="text-base-content/60">Producto no encontrado.</p>
                    </div>
                </section>
            </ProductDetailTemplate>
        );
    }

    return (
        <>
            <ProductDetailTemplate>
                <ProductDetailHero
                    product={product}
                    aiOverview={aiOverview}
                    aiOverviewStatus={aiOverviewStatus}
                />
                <ProductSpecificationsSection
                    specifications={product.productSpecifications || []}
                />
            </ProductDetailTemplate>
            <AIChatWidget />
        </>
    );
}
