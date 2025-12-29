"use client";
import React, { useState, useEffect } from "react";
import Link from "next/link";
import { useParams } from "next/navigation";
import Swal from "sweetalert2";

// Components
import { TrashIcon } from "@/components/icons";

// Hooks
import { useDeleteData, useGetData, usePutData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";
import { useBase64 } from "@/hooks/uesBase64";
import Loading from "@/components/loading";

export const dynamic = "force-dynamic";

export default function Product() {
    const { id } = useParams();

    const [specs, setSpecs] = useState([{ name: "", value: "" }]);
    const [editable, setEditable] = useState(false);

    const {
        data: product,
        loading: loadingProduct,
        reload: reloadProduct,
    } = useGetData(`/products/${id}`);

    useEffect(() => {
        if (product) {
            setSpecs(
                product.specs.map((spec) => ({
                    name: spec.spec_key,
                    value: spec.spec_value,
                }))
            );
        }
    }, [product]);

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
                product_id: product.product_id,
                product_name: json.product_name,
                product_description: json.product_description,
                product_price: json.product_price,
                product_discount: json.product_discount,
            },
            multimedias: multimediasdata.length == 0 ? null : multimediasdata,
            product_image:
                json.product_image.size == 0 ? null : await useBase64(json.product_image),
        };

        const validation = useValidateform(data.product, "update-product-form");

        if (!validation.success) return;

        if (specsArray.length == 0) return Swal.fire("Error", "Debes agregar al menos una especificación", "error");

        const response = await usePutData(`/products/${product.product_id}`, data);

        if (response.success) {
            reloadProduct();
            setEditable(false);
        }
    };

    const handleDeleteMedia = (id) => {
        Swal.fire({
            icon: "warning",
            title: "Esta acción será irrevertible",
            text: "Si eliminas este elemento, no se verá más.",
            showConfirmButton: true,
            showDenyButton: true,
            confirmButtonColor: "red",
            denyButtonColor: "green",
            confirmButtonText: "Borrar",
            denyButtonText: "Cancelar",
        }).then(async (result) => {
            if (result.isConfirmed) {
                const response = await useDeleteData(`/medias/${id}`);

                if (response.success) {
                    reloadProduct();
                }
            }
        });
    };

    useEffect(() => {
        document.title = "Editar producto | AC Computers";
    }, []);

    if (loadingProduct) return <Loading />;
    return (
        <section className="w-full px-3">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-[100px]">
                <div className="space-y-10">
                    <h2 className="text-4xl font-extrabold tracking-tight">Perfil producto:</h2>
                    <div className="flex flex-col md:flex-row gap-10">
                        <div className="w-full mx-auto max-w-[400px] space-y-5">
                            <article className="card h-fit bg-black/10 w-full [&_p]:grow-0">
                                <div className="card-body">
                                    <figure className="w-full max-w-[400px] aspect-square">
                                        <img
                                            src={product.product_image_url}
                                            alt={`Imagen del producto ${product.product_name}`}
                                            className="w-full h-full object-contain rounded"
                                        />
                                    </figure>
                                    <h2 className="card-title font-extrabold text-3xl">
                                        {product.product_name}
                                    </h2>
                                    <p className="text-sm">{product.product_description}</p>
                                    <div className="flex w-full justify-between">
                                        <p>
                                            $
                                            {parseInt(product.product_price).toLocaleString(
                                                "es-CO"
                                            )}
                                        </p>
                                        {product.product_discount > 0 && (
                                            <div className="indicator">
                                                <span className="indicator-item badge badge-primary py-1 h-auto font-semibold">
                                                    {product.product_discount}%
                                                </span>
                                                <p className="pr-4">
                                                    $
                                                    {parseInt(
                                                        product.product_price *
                                                            (1 - product.product_discount / 100)
                                                    ).toLocaleString("es-CO")}
                                                </p>
                                            </div>
                                        )}
                                    </div>
                                    <div className="flex gap-2 pt-5">
                                        <button
                                            onClick={() => setEditable(!editable)}
                                            className="btn btn-primary btn-outline grow"
                                        >
                                            {editable ? "Cancelar" : "Editar"}
                                        </button>
                                        <Link
                                            href={`/product/${product.product_id}`}
                                            className="btn btn-primary"
                                        >
                                            Perfil
                                        </Link>
                                    </div>
                                </div>
                            </article>
                            <article className="card h-fit bg-black/10 w-full h-fit">
                                <div className="card-body">
                                    <h2 className="card-title font-extrabold text-3xl">
                                        Multimedias
                                    </h2>
                                    {product.multimedias.length == 0 && (
                                        <p className="text-sm">
                                            No se han agregado imagenes del producto
                                        </p>
                                    )}
                                    {product.multimedias.map((multimedia) => (
                                        <figure
                                            key={multimedia.media_id}
                                            className="w-full max-w-[400px] aspect-video bg-black/30 rounded-lg relative overflow-hidden group"
                                        >
                                            <img
                                                src={multimedia.media_url}
                                                alt={`Imagen del producto ${product.product_name}`}
                                                className="w-full h-full object-contain rounded  group-hover:scale-110 duration-300"
                                            />
                                            <button
                                                onClick={() =>
                                                    handleDeleteMedia(multimedia.media_id)
                                                }
                                                className="btn btn-sm btn-error text-white absolute bottom-2 right-2 tooltip tooltip-left"
                                                data-tip="Eliminar multimedia"
                                            >
                                                <TrashIcon size={18} />
                                            </button>
                                        </figure>
                                    ))}
                                </div>
                            </article>
                        </div>
                        <div className="card bg-black/10 w-full h-fit">
                            <div className="card-body">
                                <h2 className="card-title font-extrabold text-3xl">Edición</h2>
                                <form onSubmit={handleFormSubmit} className="space-y-2">
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                Nombre
                                            </span>
                                        </label>
                                        <input
                                            defaultValue={product.product_name}
                                            className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                            name="product_name"
                                            disabled={!editable}
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
                                            defaultValue={product.product_description}
                                            placeholder="Ingresa una descripción"
                                            className="textarea textarea-sm textarea-bordered focus:textarea-primary focus:outline-0 w-full h-32 resize-none leading-[1.3] disabled:textarea-bordered"
                                            disabled={!editable}
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
                                                defaultValue={product.product_price}
                                                className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                                name="product_price"
                                                disabled={!editable}
                                            />
                                        </fieldset>
                                        <fieldset className="fieldset grow">
                                            <label className="label">
                                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                    Descuento
                                                </span>
                                            </label>
                                            <input
                                                defaultValue={product.product_discount}
                                                className="input input-bordered focus:outline-0 focus:input-primary disabled:input-bordered"
                                                name="product_discount"
                                                disabled={!editable}
                                            />
                                        </fieldset>
                                    </div>
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                Imagen
                                            </span>
                                        </label>
                                        <input
                                            type="file"
                                            className="file-input file-input-bordered file-input-primary disabled:input-bordered w-full"
                                            name="product_image"
                                            disabled={!editable}
                                            accept="image/*"
                                        />
                                    </fieldset>
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-1">
                                                Multimedias
                                            </span>
                                        </label>
                                        <input
                                            type="file"
                                            className="file-input file-input-bordered file-input-primary disabled:input-bordered w-full"
                                            name="multimedias"
                                            disabled={!editable}
                                            multiple
                                            accept="image/*"
                                        />
                                    </fieldset>
                                    <fieldset className="fieldset">
                                        <label className="label">
                                            <span className="label-text font-semibold">
                                                Especificaciones:
                                            </span>
                                        </label>
                                        <div className="flex flex-col gap-2">
                                            {specs.map((spec, i) => (
                                                <div key={i} className="flex items-center gap-2">
                                                    <input
                                                        name={`specs[${i}].name`}
                                                        placeholder={`Nombre ${i + 1}`}
                                                        className="input input-sm input-bordered focus:input-primary focus:outline-0 w-1/2 disabled:input-bordered"
                                                        value={specs[i].name}
                                                        onChange={(e) => {
                                                            const newSpecs = [...specs];
                                                            newSpecs[i].name = e.target.value;
                                                            setSpecs(newSpecs);
                                                        }}
                                                        disabled={!editable}
                                                    />
                                                    <input
                                                        name={`specs[${i}].value`}
                                                        placeholder={`Valor ${i + 1}`}
                                                        className="input input-sm input-bordered focus:input-primary focus:outline-0 w-1/2 disabled:input-bordered"
                                                        value={specs[i].value}
                                                        onChange={(e) => {
                                                            const newSpecs = [...specs];
                                                            newSpecs[i].value = e.target.value;
                                                            setSpecs(newSpecs);
                                                        }}
                                                        disabled={!editable}
                                                    />
                                                    <button
                                                        type="button"
                                                        className="btn btn-sm btn-error disabled:input-bordered"
                                                        onClick={() => {
                                                            if (specs.length === 1) return setSpecs([{ name: "", value: "" }]);
                                                            let newSpecs = [
                                                                ...specs.filter(
                                                                    (_, index) => index !== i
                                                                ),
                                                            ];
                                                            setSpecs(newSpecs);
                                                        }}
                                                        disabled={!editable}
                                                    >
                                                        Eliminar
                                                    </button>
                                                </div>
                                            ))}
                                            <button
                                                type="button"
                                                className="btn btn-sm btn-primary btn-outline "
                                                onClick={() =>
                                                    setSpecs([...specs, { name: "", value: "" }])
                                                }
                                                disabled={!editable}
                                            >
                                                Agregar Especificación
                                            </button>
                                        </div>
                                    </fieldset>
                                    {editable && (
                                        <fieldset className="fieldset pt-5">
                                            <button className="btn btn-primary">Guardar</button>
                                        </fieldset>
                                    )}
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}
