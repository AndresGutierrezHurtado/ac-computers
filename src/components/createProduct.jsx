"use client";
import React, { useState } from "react";
import Swal from "sweetalert2";

// Components
import { UploadIcon } from "@/components/icons.jsx";

// Hooks
import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";
import { useBase64 } from "@/hooks/uesBase64";

export default function CreateProduct({ reloadProducts }) {
    const [specs, setSpecs] = useState([{ name: "", value: "" }]);

    const handleFormSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData(e.target);
        const json = Object.fromEntries(formData);

        let specsArray = specs.map((_, i) => ({
            spec_key: formData.get(`specs[${i}].name`),
            spec_value: formData.get(`specs[${i}].value`),
        }));

        specsArray = specsArray.filter(
            (spec) => spec.spec_key.length > 0 && spec.spec_value.length > 0
        );

        const multimediasdata = await Promise.all(
            formData
                .getAll("multimedias")
                .filter((file) => file.size !== 0)
                .map(async (file) => await useBase64(file))
        );

        const data = {
            specs: specsArray.filter((spec) => spec.spec_key && spec.spec_value),
            product: {
                product_name: json.product_name,
                product_description: json.product_description,
                product_price: json.product_price,
                product_discount: json.product_discount,
                category_id: json.category_id,
            },
            multimedias: multimediasdata.length == 0 ? null : multimediasdata,
            product_image:
                json.product_image.size == 0 ? null : await useBase64(json.product_image),
        };

        const validation = useValidateform(data.product, "create-product-form");

        if (specsArray.length == 0) {
            validation.success = false;
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Debes ingresar al menos una especificación",
                timer: 8000,
            });
        }

        if (!validation.success) return;

        Swal.fire({
            icon: "info",
            title: "¿Estas seguro de crear el producto?",
            text: "El producto se creara con los datos ingresados",
            showDenyButton: true,
            confirmButtonText: "Crear",
            denyButtonText: "Cancelar",
            confirmButtonColor: "#3085d6",
            denyButtonColor: "#d33",
        }).then(async (result) => {
            if (result.isConfirmed) {
                const response = await usePostData("/products", data);

                if (response.success) {
                    e.target.closest("dialog").close();
                    e.target.reset();
                    reloadProducts();
                }
            }
        });
    };

    return (
        <>
            <dialog id="create-product" className="modal">
                <div className="modal-box w-full max-w-xl">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
                            ✕
                        </button>
                    </form>
                    <h3 className="font-extrabold text-2xl tracking-tight">Crea un producto:</h3>
                    <p className="py-4">
                        Para cerrar presiona <kbd className="kbd kbd-sm">Esc</kbd> o haz click fuera
                        de la ventana modal.
                    </p>
                    <form onSubmit={handleFormSubmit} className="space-y-2">
                        {/* Campos del producto */}
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Nombre:
                                </span>
                            </label>
                            <input
                                name="product_name"
                                placeholder="Ingresa el nombre"
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
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
                                className="textarea textarea-sm textarea-bordered focus:textarea-primary focus:outline-0 w-full h-32 resize-none leading-[1.3]"
                            ></textarea>
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Precio:
                                </span>
                            </label>
                            <input
                                name="product_price"
                                placeholder="Ingresa el precio del producto"
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Descuento:
                                </span>
                            </label>
                            <input
                                name="product_discount"
                                placeholder="Ingresa un porcentaje de descuento 0-100%"
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Categoría:
                                </span>
                            </label>
                            <select
                                name="category_id"
                                className="select select-sm select-bordered focus:select-primary focus:outline-0 w-full"
                            >
                                <option value="1">Computador</option>
                                <option value="2">Componente</option>
                            </select>
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Imagen principal:
                                </span>
                            </label>
                            <input
                                name="product_image"
                                type="file"
                                className="file-input file-input-primary file-input-sm file-input-bordered w-full"
                                accept="image/*"
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Multimedias:
                                </span>
                            </label>
                            <input
                                name="multimedias"
                                type="file"
                                className="file-input file-input-primary file-input-sm file-input-bordered w-full"
                                accept="image/*"
                                multiple
                                max={8}
                                maxLength={8}
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold">Especificaciones:</span>
                            </label>
                            <div className="flex flex-col gap-2">
                                {specs.map((_, i) => (
                                    <div key={i} className="flex items-center gap-2">
                                        <input
                                            name={`specs[${i}].name`}
                                            placeholder={`Nombre ${i + 1}`}
                                            className="input input-sm input-bordered focus:input-primary focus:outline-0 w-1/2"
                                            value={specs[i].name}
                                            onChange={(e) => {
                                                const newSpecs = [...specs];
                                                newSpecs[i].name = e.target.value;
                                                setSpecs(newSpecs);
                                            }}
                                        />
                                        <input
                                            name={`specs[${i}].value`}
                                            placeholder={`Valor ${i + 1}`}
                                            className="input input-sm input-bordered focus:input-primary focus:outline-0 w-1/2"
                                            value={specs[i].value}
                                            onChange={(e) => {
                                                const newSpecs = [...specs];
                                                newSpecs[i].value = e.target.value;
                                                setSpecs(newSpecs);
                                            }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-sm btn-error"
                                            onClick={() => {
                                                if (specs.length === 1)
                                                    return setSpecs([{ name: "", value: "" }]);

                                                const newSpecs = [
                                                    ...specs.filter((spec) => spec !== specs[i]),
                                                ];

                                                setSpecs(newSpecs);
                                            }}
                                        >
                                            Eliminar
                                        </button>
                                    </div>
                                ))}
                                <button
                                    type="button"
                                    className="btn btn-sm btn-outline btn-primary mt-2"
                                    onClick={() => setSpecs([...specs, { name: "", value: "" }])}
                                >
                                    Agregar Especificación
                                </button>
                            </div>
                        </fieldset>

                        <fieldset className="fieldset pt-5">
                            <button type="submit" className="btn btn-primary btn-sm w-full">
                                <UploadIcon size={20} />
                                Subir
                            </button>
                        </fieldset>
                    </form>
                </div>
                <form method="dialog" className="modal-backdrop bg-black/80 backdrop-blur-[1px]">
                    <button>close</button>
                </form>
            </dialog>
        </>
    );
}
