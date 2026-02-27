"use client";

import { useEffect, useMemo, useState } from "react";
import ProductImage from "@/atoms/ProductImage";
import ProductPrice from "@/atoms/ProductPrice";
import Badge from "@/atoms/Badge";

export default function ProductDetailHero({ product }) {
    const images = product?.images || [];
    const defaultImage = useMemo(() => {
        const main = images.find((img) => img.isMain) || images[0];
        return main?.url || "/placeholder-image.png";
    }, [images]);
    const [activeImage, setActiveImage] = useState(defaultImage);

    useEffect(() => {
        setActiveImage(defaultImage);
    }, [defaultImage]);

    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                <div className="flex flex-col md:flex-row gap-10">
                    <div className="flex-none w-full md:w-[450px]">
                        <ProductImage src={activeImage} alt={product.name} />
                        {images.length > 1 && (
                            <div className="grid grid-cols-5 gap-2 mt-4">
                                {images.map((img, index) => {
                                    const url = img?.url || "/placeholder-image.png";
                                    const isActive = url === activeImage;
                                    return (
                                        <button
                                            key={img.id || url || index}
                                            type="button"
                                            onClick={() => setActiveImage(url)}
                                            className={`aspect-square rounded-lg overflow-hidden border cursor-pointer hover:bg-base-content/10 transition-colors duration-200 ${
                                                isActive
                                                    ? "border-primary"
                                                    : "border-base-content/10"
                                            }`}
                                        >
                                            <img
                                                src={url}
                                                alt={`${product.name} ${index + 1}`}
                                                className="w-full h-full object-contain bg-base-content/10"
                                            />
                                        </button>
                                    );
                                })}
                            </div>
                        )}
                    </div>
                    <div className="grow flex flex-col gap-3">
                        <p className="leading-none text-primary font-bold">
                            {product.brand?.name || "AC Computers"}
                        </p>
                        <h1 className="text-5xl font-extrabold tracking-tight">
                            {product.name}
                        </h1>
                        {product.subCategory?.name && (
                            <p className="text-base-content/70">
                                Subcategoria: {product.subCategory.name}
                            </p>
                        )}
                        <p className="text-lg grow">{product.description}</p>
                        <div className="flex items-center gap-3">
                            <ProductPrice price={product.price} discount={product.discount} />
                            {product.discount > 0 && (
                                <Badge>{product.discount}% OFF</Badge>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}
