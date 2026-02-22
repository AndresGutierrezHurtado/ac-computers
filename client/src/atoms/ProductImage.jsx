export default function ProductImage({ src, alt }) {
    return (
        <figure className="w-full aspect-square bg-base-100 p-4 rounded-xl flex items-center justify-center overflow-hidden">
            <img
                src={src || "/placeholder-image.png"}
                alt={alt}
                className="w-full h-full object-contain hover:scale-105 transition-transform duration-300"
            />
        </figure>
    );
}
