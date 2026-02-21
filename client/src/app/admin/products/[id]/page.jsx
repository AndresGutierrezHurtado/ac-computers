export default function AdminProductDetailPage() {
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-[100px]">
                <div className="space-y-10">
                    <h2 className="text-4xl font-extrabold tracking-tight">Perfil producto:</h2>
                    <div className="flex flex-col md:flex-row gap-10">
                        <div className="w-full mx-auto max-w-[400px] space-y-5">
                            <article className="card h-fit bg-black/10 w-full [&_p]:grow-0">
                                <div className="card-body">
                                    <figure className="w-full max-w-[400px] aspect-square"></figure>
                                    <h2 className="card-title font-extrabold text-3xl">Producto</h2>
                                    <p className="text-sm">Descripción del producto</p>
                                </div>
                            </article>
                        </div>
                        <div className="card bg-black/10 w-full h-fit">
                            <div className="card-body">
                                <h2 className="card-title font-extrabold text-3xl">Edición</h2>
                                <form className="space-y-2">
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                Nombre
                                            </span>
                                        </label>
                                        <input
                                            className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                            name="product_name"
                                            disabled
                                        />
                                    </fieldset>
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                                Descripción:
                                            </span>
                                        </label>
                                        <textarea
                                            name="product_description"
                                            placeholder="Ingresa una descripción"
                                            className="textarea textarea-sm textarea-bordered focus:textarea-primary focus:outline-0 w-full h-32 resize-none leading-[1.3] disabled:textarea-bordered"
                                            disabled
                                        ></textarea>
                                    </fieldset>
                                    <div className="flex flex-col sm:flex-row w-full gap-4">
                                        <fieldset className="fieldset grow">
                                            <label className="label">
                                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                    Precio
                                                </span>
                                            </label>
                                            <input
                                                className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                                name="product_price"
                                                disabled
                                            />
                                        </fieldset>
                                        <fieldset className="fieldset grow">
                                            <label className="label">
                                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                    Descuento
                                                </span>
                                            </label>
                                            <input
                                                className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                                name="product_discount"
                                                disabled
                                            />
                                        </fieldset>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}
