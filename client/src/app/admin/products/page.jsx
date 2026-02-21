export default function AdminProductsPage() {
    return (
        <>
            <section className="w-full px-3">
                <div className="w-full max-w-[1200px] mx-auto mt-[100px]">
                    <div className="space-y-5">
                        <div className="flex justify-between items-center w-full">
                            <h1 className="text-3xl font-bold mb-4">Administrar productos</h1>
                            <button className="btn btn-primary btn-outline">
                                + Crear Producto
                            </button>
                        </div>
                        <div className="card bg-zinc-950/30 rounded [&_p]:grow-0">
                            <div className="card-body p-4">
                                <div className="flex flex-col sm:flex-row gap-4 justify-between items-center w-full">
                                    <h2 className="text-3xl font-bold">Productos</h2>
                                    <label className="input input-sm input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full max-w-sm h-auto py-1">
                                        <input
                                            className="grow group"
                                            placeholder="Buscar productos"
                                        />
                                    </label>
                                </div>
                            </div>
                            <div className="w-full overflow-x-auto">
                                <table className="w-full table rounded">
                                    <thead className="transparent bg-zinc-950/30">
                                        <tr className="text-[15px] [&>*]:py-3">
                                            <th>ID</th>
                                            <th>Nombre</th>
                                            <th>Precio</th>
                                            <th>Descuento</th>
                                            <th>Tipo</th>
                                            <th>Fecha</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr className="[&>*]:py-4 text-center text-xl">
                                            <td colSpan={7}>No hay productos...</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}
