import Link from "next/link";
import ProductImage from "@/atoms/ProductImage";
import ProductPrice from "@/atoms/ProductPrice";

export default function ProductCard({ product }) {
    // Find main image or fallback
    const mainImage = product.images?.find((img) => img.isMain) || product.images?.[0];
    const imageUrl = mainImage?.url;

    return (
        <div className="card rounded-none [&_p]:grow-0 relative">
            <Link href={`/products/${product.id}`} className="w-full aspect-square group bg-base-content/10 overflow-hidden rounded-lg">
                <img
                    src={imageUrl || "/placeholder-image.png"}
                    alt={product.name}
                    className="w-full h-full object-contain p-4 group-hover:scale-105 transition-all duration-300"
                />
            </Link>

            <div className="card-body p-2 gap-0 text-center flex flex-col items-center">
                <p className="text-sm font-medium text-base-content/60 uppercase tracking-widest">
                    {product.brand?.name || "AC Computers"}
                </p>
                <Link
                    href={`/product/${product.id}`}
                    className="font-semibold text-lg leading-none tracking-tight scale-y-105 hover:text-base-content/60 hover:underline mt-1 mb-1"
                >
                    {product.name}
                </Link>
                <div className="flex justify-center gap-2 items-center">
                    <ProductPrice price={product.price} discount={product.discount} />
                </div>
            </div>

            {product.discount > 0 && (
                <div className="absolute top-2 right-2">
                    <div className="w-fit px-2 h-9 bg-primary rounded-full flex items-center justify-center text-sm font-semibold text-white">
                        {product.discount}%
                    </div>
                </div>
            )}
        </div>
    );
}
