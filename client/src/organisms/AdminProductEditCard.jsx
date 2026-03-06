import AdminFormField from "@/molecules/AdminFormField";

export default function AdminProductEditCard() {
    return (
        <div className="card bg-black/10 w-full h-fit">
            <div className="card-body">
                <h2 className="card-title font-extrabold text-3xl">Edición</h2>
                <form className="space-y-2">
                    <AdminFormField label="Nombre" name="product_name" />
                    <AdminFormField
                        label="Descripción:"
                        name="product_description"
                        placeholder="Ingresa una descripción"
                        as="textarea"
                    />
                    <div className="flex flex-col sm:flex-row w-full gap-4">
                        <div className="grow">
                            <AdminFormField label="Precio" name="product_price" />
                        </div>
                        <div className="grow">
                            <AdminFormField label="Descuento" name="product_discount" />
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}
