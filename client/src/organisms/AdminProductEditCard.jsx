import TextField from "@/molecules/TextField";
import TextareaField from "@/molecules/TextareaField";

export default function AdminProductEditCard() {
    return (
        <div className="card bg-black/10 w-full h-fit">
            <div className="card-body">
                <h2 className="card-title font-extrabold text-3xl">Edición</h2>
                <form className="space-y-2">
                    <TextField label="Nombre" name="product_name" className="input-sm input-bordered" />
                    <TextareaField
                        label="Descripción:"
                        name="product_description"
                        placeholder="Ingresa una descripción"
                        className="textarea-sm textarea-bordered min-h-24"
                        resizable={false}
                    />
                    <div className="flex flex-col sm:flex-row w-full gap-4">
                        <div className="grow">
                            <TextField label="Precio" name="product_price" className="input-sm input-bordered" />
                        </div>
                        <div className="grow">
                            <TextField label="Descuento" name="product_discount" className="input-sm input-bordered" />
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}
