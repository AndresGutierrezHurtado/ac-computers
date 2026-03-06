export default function AdminProductPreviewCard() {
    return (
        <article className="card h-fit bg-black/10 w-full [&_p]:grow-0">
            <div className="card-body">
                <figure className="w-full max-w-[400px] aspect-square"></figure>
                <h2 className="card-title font-extrabold text-3xl">Producto</h2>
                <p className="text-sm">Descripción del producto</p>
            </div>
        </article>
    );
}
