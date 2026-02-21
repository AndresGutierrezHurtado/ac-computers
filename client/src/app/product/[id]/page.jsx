export const metadata = {
    title: "Producto | AC Computers",
};

export default function ProductDetailPage() {
    return (
        <main className="flex flex-col gap-[80px]">
            <section className="w-full px-3">
                <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                    <div className="flex flex-col md:flex-row gap-10">
                        <div className="flex-none">
                            <div className="w-[450px] aspect-square bg-base-200 rounded-lg"></div>
                        </div>
                        <div className="grow flex flex-col gap-2">
                            <p className="leading-none text-primary font-bold">AC Computers</p>
                            <h1 className="text-5xl font-extrabold tracking-tight">Producto</h1>
                            <p className="text-lg grow">Descripción del producto</p>
                        </div>
                    </div>
                </div>
            </section>
            <section className="w-full px-3">
                <div className="w-full max-w-[1200px] mx-auto py-10">
                    <div className="space-y-2">
                        <h2 className="text-2xl font-extrabold tracking-tight">
                            Especificaciones del producto:
                        </h2>
                        {/* Specs will be rendered here */}
                    </div>
                </div>
            </section>
        </main>
    );
}
