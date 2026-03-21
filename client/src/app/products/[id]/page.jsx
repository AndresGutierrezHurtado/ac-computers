"use client";

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { FetchData, useGetData } from "@/hooks/useClientData";
import ProductDetailTemplate from "@/templates/ProductDetailTemplate";
import ProductDetailHero from "@/organisms/ProductDetailHero";
import ProductSpecificationsSection from "@/organisms/ProductSpecificationsSection";
import AIChatWidget from "@/organisms/AIChatWidget";

export default function ProductDetailPage() {
    const params = useParams();
    const productId = Array.isArray(params?.id) ? params.id[0] : params?.id;
    const { data: product, loading } = useGetData(`/products/${productId}`);

    const [aiOverview, setAiOverview] = useState(null);
    const [aiOverviewLoading, setAiOverviewLoading] = useState(false);
    const [aiOverviewError, setAiOverviewError] = useState(false);

    useEffect(() => {
        if (!productId) return undefined;
        let cancelled = false;
        setAiOverview(null);
        setAiOverviewError(false);
        setAiOverviewLoading(true);
        FetchData(`/products/${productId}/overview`)
            .then((res) => {
                if (cancelled) return;
                if (res?.success && typeof res?.data?.overview === "string") {
                    setAiOverview(res.data.overview);
                } else {
                    setAiOverviewError(true);
                }
            })
            .catch(() => {
                if (!cancelled) setAiOverviewError(true);
            })
            .finally(() => {
                if (!cancelled) setAiOverviewLoading(false);
            });
        return () => {
            cancelled = true;
        };
    }, [productId]);

    useEffect(() => {
        if (!product || !product.name) return;
        document.title = `${product.name} - AC Computers`;
    }, [product]);

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
                    aiOverviewLoading={aiOverviewLoading}
                    aiOverviewError={aiOverviewError}
                />
                <ProductSpecificationsSection
                    specifications={product.productSpecifications || []}
                />
            </ProductDetailTemplate>
            <AIChatWidget />
        </>
    );
}
