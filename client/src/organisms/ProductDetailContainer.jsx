"use client";

import { useParams } from "next/navigation";
import { useGetData } from "@/hooks/useClientData";
import ProductDetailTemplate from "@/templates/ProductDetailTemplate";
import ProductDetailHero from "@/organisms/ProductDetailHero";
import ProductSpecificationsSection from "@/organisms/ProductSpecificationsSection";

export default function ProductDetailContainer() {
    const params = useParams();
    const productId = Array.isArray(params?.id) ? params.id[0] : params?.id;
    const { data: product, loading } = useGetData(`/products/${productId}`);

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
        <ProductDetailTemplate>
            <ProductDetailHero product={product} />
            <ProductSpecificationsSection
                specifications={product.productSpecifications || []}
            />
        </ProductDetailTemplate>
    );
}
