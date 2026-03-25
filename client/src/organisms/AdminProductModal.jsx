"use client";

import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";

import Modal from "@/molecules/Modal";
import TextField from "@/molecules/TextField";
import TextareaField from "@/molecules/TextareaField";
import SelectField from "@/molecules/SelectField";
import AdminSpecRow from "@/molecules/AdminSpecRow";

import { useGetData, usePostFormData, usePutFormData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

const CONDITION_OPTIONS = [
    { value: "new", label: "Nuevo" },
    { value: "used", label: "Usado" },
    { value: "refurbished", label: "Reacondicionado" },
    { value: "for_parts", label: "Para repuestos" },
];

const emptyForm = {
    name: "",
    description: "",
    price: "",
    discount: "0",
    condition: "new",
    brandId: "",
    categoryId: "",
    subCategoryId: "",
};

export default function AdminProductModal({
    open,
    mode = "view",
    product,
    onClose,
    onSaved,
    onEdit,
    onDelete,
}) {
    const [form, setForm] = useState(emptyForm);
    const [existingImages, setExistingImages] = useState([]);
    const [newImages, setNewImages] = useState([]);
    const [removedImageIds, setRemovedImageIds] = useState([]);
    const [mainSelection, setMainSelection] = useState(null);
    const [specs, setSpecs] = useState([]);
    const [submitting, setSubmitting] = useState(false);

    const { data: brands } = useGetData("/brands");
    const { data: categories } = useGetData("/categories");

    const specsEndpoint = useMemo(() => {
        if (form.subCategoryId) {
            return `/specifications?subCategoryId=${form.subCategoryId}`;
        }
        return null;
    }, [form.subCategoryId]);

    const { data: specifications } = useGetData(specsEndpoint);

    const subCategoryEndpoint = useMemo(() => {
        if (form.categoryId) {
            return `/subcategories?categoryId=${form.categoryId}`;
        }
        return "/subcategories";
    }, [form.categoryId]);

    const { data: subCategories } = useGetData(subCategoryEndpoint);

    useEffect(() => {
        if (!open) return;
        if (!product) {
            setForm(emptyForm);
            setSpecs([{ id: "", specificationId: "", value: "", specificationValueId: "" }]);
            setExistingImages([]);
            setRemovedImageIds([]);
            setNewImages((prev) => {
                prev.forEach((img) => URL.revokeObjectURL(img.previewUrl));
                return [];
            });
            setMainSelection(null);
            return;
        }

        setForm({
            name: product.name || "",
            description: product.description || "",
            price: product.price?.toString() || "",
            discount: product.discount?.toString() || "0",
            condition: product.condition || "new",
            brandId: product.brand?.id?.toString() || "",
            categoryId: product.subCategory?.categoryId?.toString() || "",
            subCategoryId: product.subCategory?.id?.toString() || "",
        });

        const mappedSpecs = (product.productSpecifications || []).map((spec) => ({
            id: spec.id?.toString() || "",
            specificationId: spec.specification?.id?.toString() || "",
            value: spec.value || "",
            specificationValueId: "",
        }));

        setSpecs(
            mappedSpecs.length > 0
                ? mappedSpecs
                : [{ id: "", specificationId: "", value: "", specificationValueId: "" }],
        );
        const images = product.images || [];
        setExistingImages(images);
        setRemovedImageIds([]);
        setNewImages((prev) => {
            prev.forEach((img) => URL.revokeObjectURL(img.previewUrl));
            return [];
        });
        const mainImage = images.find((img) => img.isMain) || images[0];
        setMainSelection(mainImage ? { kind: "existing", id: mainImage.id } : null);
    }, [open, product]);

    useEffect(() => {
        if (open) return;
        setNewImages((prev) => {
            prev.forEach((img) => URL.revokeObjectURL(img.previewUrl));
            return [];
        });
        setRemovedImageIds([]);
        setMainSelection(null);
    }, [open]);

    useEffect(() => {
        if (!form.categoryId) return;
        if (form.subCategoryId) return;
        const first = subCategories?.[0];
        if (first?.id) {
            setForm((prev) => ({ ...prev, subCategoryId: first.id.toString() }));
        }
    }, [form.categoryId, form.subCategoryId, subCategories]);

    const readOnly = mode === "view";

    const handleChange = (field) => (event) => {
        setForm((prev) => ({ ...prev, [field]: event.target.value }));
    };

    const handleAddImages = (event) => {
        const files = Array.from(event.target.files || []);
        if (files.length === 0) return;
        const mapped = files
            .filter((file) => file && file.size > 0)
            .map((file) => ({
                file,
                previewUrl: URL.createObjectURL(file),
            }));
        if (mapped.length === 0) return;

        setNewImages((prev) => {
            const next = [...prev, ...mapped];
            if (!mainSelection && existingImages.length === 0) {
                setMainSelection({ kind: "new", index: prev.length });
            }
            return next;
        });

        event.target.value = "";
    };

    const handleRemoveExistingImage = (imageId) => {
        setExistingImages((prev) => {
            const next = prev.filter((img) => img.id !== imageId);
            setRemovedImageIds((prevIds) => [...prevIds, imageId]);
            setMainSelection((current) => {
                if (!current) return current;
                if (current.kind === "existing" && current.id === imageId) {
                    if (next.length > 0) {
                        return { kind: "existing", id: next[0].id };
                    }
                    if (newImages.length > 0) {
                        return { kind: "new", index: 0 };
                    }
                    return null;
                }
                return current;
            });
            return next;
        });
    };

    const handleRemoveNewImage = (index) => {
        setNewImages((prev) => {
            const target = prev[index];
            if (target?.previewUrl) {
                URL.revokeObjectURL(target.previewUrl);
            }
            const next = prev.filter((_, i) => i !== index);
            setMainSelection((current) => {
                if (!current) return current;
                if (current.kind === "new") {
                    if (current.index === index) {
                        if (existingImages.length > 0) {
                            return { kind: "existing", id: existingImages[0].id };
                        }
                        if (next.length > 0) {
                            return { kind: "new", index: 0 };
                        }
                        return null;
                    }
                    if (current.index > index) {
                        return { ...current, index: current.index - 1 };
                    }
                }
                return current;
            });
            return next;
        });
    };

    const handleSelectExistingMain = (id) => {
        setMainSelection({ kind: "existing", id });
    };

    const handleSelectNewMain = (index) => {
        setMainSelection({ kind: "new", index });
    };

    const handleSpecChange = (index, nextSpec) => {
        setSpecs((prev) => prev.map((item, i) => (i === index ? nextSpec : item)));
    };

    const handleSpecRemove = (index) => {
        setSpecs((prev) => prev.filter((_, i) => i !== index));
    };

    const handleSpecAdd = () => {
        setSpecs((prev) => [
            ...prev,
            { id: "", specificationId: "", value: "", specificationValueId: "" },
        ]);
    };

    const validateSpecs = () => {
        const cleaned = specs
            .map((spec) => ({
                ...spec,
                specificationId: spec.specificationId?.trim(),
                value: spec.value?.trim(),
            }))
            .filter((spec) => spec.specificationId || spec.value || spec.specificationValueId);

        if (cleaned.length === 0) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Debes agregar al menos una especificación",
                timer: 8000,
            });
            return null;
        }

        const invalid = cleaned.find((spec) => !spec.specificationId || !spec.value);
        if (invalid) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Cada especificación debe tener ID y valor",
                timer: 8000,
            });
            return null;
        }

        return cleaned;
    };

    const buildFormData = (specList) => {
        const formData = new FormData();
        formData.append("name", form.name);
        formData.append("description", form.description);
        formData.append("price", form.price);
        formData.append("discount", form.discount);
        formData.append("condition", form.condition);
        formData.append("brandId", form.brandId);
        formData.append("subCategoryId", form.subCategoryId);

        newImages.forEach((image) => {
            formData.append("images", image.file);
        });

        Array.from(new Set(removedImageIds)).forEach((id) => {
            formData.append("removeImageIds", id);
        });

        if (mainSelection?.kind === "existing" && mainSelection.id) {
            formData.append("mainImageId", mainSelection.id);
        }
        if (mainSelection?.kind === "new" && Number.isInteger(mainSelection.index)) {
            formData.append("mainImageIndex", mainSelection.index);
        }

        specList.forEach((spec, index) => {
            if (spec.id) {
                formData.append(`specifications[${index}].id`, spec.id);
            }
            formData.append(`specifications[${index}].specificationId`, spec.specificationId);
            formData.append(`specifications[${index}].value`, spec.value);
            if (spec.specificationValueId) {
                formData.append(
                    `specifications[${index}].specificationValueId`,
                    spec.specificationValueId,
                );
            }
        });

        return formData;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (readOnly || submitting) return;
        setSubmitting(true);

        const validation = useValidateform(
            {
                name: form.name,
                description: form.description,
                price: form.price,
                discount: form.discount,
                condition: form.condition,
                brandId: form.brandId,
                subCategoryId: form.subCategoryId,
            },
            mode === "create" ? "admin-create-product-form" : "admin-update-product-form",
        );

        if (!validation.success) {
            setSubmitting(false);
            return;
        }

        if (mode === "create" && newImages.length === 0) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Debes cargar al menos una imagen",
                timer: 8000,
            });
            setSubmitting(false);
            return;
        }

        const validSpecs = validateSpecs();
        if (!validSpecs) {
            setSubmitting(false);
            return;
        }

        const formData = buildFormData(validSpecs);

        const response =
            mode === "create"
                ? await usePostFormData("/products", formData)
                : await usePutFormData(`/products/${product.id}`, formData);

        if (response?.success) {
            onSaved?.();
            onClose();
        }

        setSubmitting(false);
    };

    const brandOptions = (brands || []).map((brand) => ({
        value: brand.id.toString(),
        label: brand.name,
    }));

    const categoryOptions = (categories || []).map((category) => ({
        value: category.id.toString(),
        label: category.name,
    }));

    const subCategoryOptions = (subCategories || []).map((subCategory) => ({
        value: subCategory.id.toString(),
        label: subCategory.name,
    }));

    if (open && mode !== "create" && !product) {
        return (
            <Modal title="Detalle producto" open={open} onClose={onClose}>
                <p className="text-center">Cargando...</p>
            </Modal>
        );
    }

    const footer =
        readOnly && product ? (
            <div className="flex flex-col-reverse sm:flex-row justify-end gap-2">
                <button
                    type="button"
                    className="btn btn-ghost text-red-400"
                    onClick={async () => {
                        if (!onDelete) return;
                        const removed = await onDelete(product.id);
                        if (removed) {
                            onClose();
                        }
                    }}
                >
                    Eliminar
                </button>
                <button
                    type="button"
                    className="btn btn-primary"
                    onClick={() => onEdit?.(product.id)}
                >
                    Editar
                </button>
            </div>
        ) : null;

    return (
        <Modal
            title={
                mode === "create"
                    ? "Crear producto"
                    : mode === "edit"
                      ? "Editar producto"
                      : "Detalle producto"
            }
            open={open}
            onClose={onClose}
            footer={footer}
        >
            <form onSubmit={handleSubmit} className="space-y-4">
                <TextField
                    label="Nombre"
                    name="name"
                    placeholder="Nombre del producto"
                    value={form.name}
                    onChange={handleChange("name")}
                    disabled={readOnly}
                    className="input-sm input-bordered"
                />
                <TextareaField
                    label="Descripción"
                    name="description"
                    placeholder="Descripción del producto"
                    value={form.description}
                    onChange={handleChange("description")}
                    disabled={readOnly}
                    resizable={false}
                    className="textarea-sm textarea-bordered min-h-24"
                />
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <TextField
                        label="Precio"
                        name="price"
                        type="number"
                        placeholder="Ej: 1500000"
                        value={form.price}
                        onChange={handleChange("price")}
                        disabled={readOnly}
                        className="input-sm input-bordered"
                    />
                    <TextField
                        label="Descuento"
                        name="discount"
                        type="number"
                        placeholder="0"
                        value={form.discount}
                        onChange={handleChange("discount")}
                        disabled={readOnly}
                        className="input-sm input-bordered"
                    />
                </div>
                <SelectField
                    label="Condición"
                    name="condition"
                    value={form.condition}
                    onChange={handleChange("condition")}
                    options={CONDITION_OPTIONS}
                    disabled={readOnly}
                    className="select-sm select-bordered"
                />
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <SelectField
                        label="Categoría"
                        name="categoryId"
                        value={form.categoryId}
                        onChange={(event) => {
                            setForm((prev) => ({
                                ...prev,
                                categoryId: event.target.value,
                                subCategoryId: "",
                            }));
                        }}
                        options={categoryOptions}
                        disabled={readOnly}
                        className="select-sm select-bordered"
                    />
                    <SelectField
                        label="Subcategoría"
                        name="subCategoryId"
                        value={form.subCategoryId}
                        onChange={handleChange("subCategoryId")}
                        options={subCategoryOptions}
                        disabled={readOnly}
                        className="select-sm select-bordered"
                    />
                </div>
                <SelectField
                    label="Marca"
                    name="brandId"
                    value={form.brandId}
                    onChange={handleChange("brandId")}
                    options={brandOptions}
                    disabled={readOnly}
                    className="select-sm select-bordered"
                />
                <div className="space-y-3">
                    <div className="flex items-center justify-between">
                        <h4 className="text-lg font-semibold">
                            Imágenes{mode === "create" ? " *" : ""}
                        </h4>
                    </div>
                    {!readOnly ? (
                        <input
                            type="file"
                            className="file-input file-input-bordered file-input-sm w-full"
                            onChange={handleAddImages}
                            accept="image/*"
                            multiple
                        />
                    ) : null}
                    <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
                        {existingImages.map((img) => {
                            const isMain =
                                mainSelection?.kind === "existing" && mainSelection.id === img.id;
                            return (
                                <div key={`existing-${img.id}`} className="border rounded-lg p-2">
                                    <div className="aspect-video w-full overflow-hidden rounded-md bg-base-200">
                                        <img
                                            src={img.url || "/placeholder-image.png"}
                                            alt={img.id ? `Imagen ${img.id}` : "Imagen"}
                                            className="h-full w-full object-cover"
                                        />
                                    </div>
                                    <div className="mt-2 flex items-center justify-between gap-2 text-sm">
                                        <label className="flex items-center gap-2">
                                            <input
                                                type="radio"
                                                name="mainImage"
                                                checked={isMain}
                                                onChange={() => handleSelectExistingMain(img.id)}
                                                disabled={readOnly}
                                            />
                                            Principal
                                        </label>
                                        {!readOnly ? (
                                            <button
                                                type="button"
                                                className="btn btn-ghost btn-xs text-red-400"
                                                onClick={() => handleRemoveExistingImage(img.id)}
                                            >
                                                Eliminar
                                            </button>
                                        ) : isMain ? (
                                            <span className="text-xs font-semibold text-primary">
                                                Principal
                                            </span>
                                        ) : null}
                                    </div>
                                </div>
                            );
                        })}
                        {newImages.map((img, index) => {
                            const isMain =
                                mainSelection?.kind === "new" && mainSelection.index === index;
                            return (
                                <div key={`new-${index}`} className="border rounded-lg p-2">
                                    <div className="aspect-video w-full overflow-hidden rounded-md bg-base-200">
                                        <img
                                            src={img.previewUrl}
                                            alt={`Imagen nueva ${index + 1}`}
                                            className="h-full w-full object-cover"
                                        />
                                    </div>
                                    <div className="mt-2 flex items-center justify-between gap-2 text-sm">
                                        <label className="flex items-center gap-2">
                                            <input
                                                type="radio"
                                                name="mainImage"
                                                checked={isMain}
                                                onChange={() => handleSelectNewMain(index)}
                                                disabled={readOnly}
                                            />
                                            Principal
                                        </label>
                                        {!readOnly ? (
                                            <button
                                                type="button"
                                                className="btn btn-ghost btn-xs text-red-400"
                                                onClick={() => handleRemoveNewImage(index)}
                                            >
                                                Eliminar
                                            </button>
                                        ) : isMain ? (
                                            <span className="text-xs font-semibold text-primary">
                                                Principal
                                            </span>
                                        ) : null}
                                    </div>
                                </div>
                            );
                        })}
                        {existingImages.length === 0 && newImages.length === 0 ? (
                            <div className="col-span-full text-sm text-center text-base-content/70">
                                No hay imágenes cargadas.
                            </div>
                        ) : null}
                    </div>
                </div>
                <div className="space-y-3">
                    <div className="flex items-center justify-between">
                        <h4 className="text-lg font-semibold">Especificaciones</h4>
                        {!readOnly ? (
                            <button
                                type="button"
                                className="btn btn-sm btn-outline"
                                onClick={handleSpecAdd}
                            >
                                + Agregar
                            </button>
                        ) : null}
                    </div>
                    <div className="space-y-3">
                        {specs.map((spec, index) => (
                            <AdminSpecRow
                                key={`${spec.id || index}`}
                                index={index}
                                spec={spec}
                                specifications={specifications || []}
                                onChange={handleSpecChange}
                                onRemove={handleSpecRemove}
                                disabled={readOnly}
                            />
                        ))}
                    </div>
                </div>
                {!readOnly ? (
                    <button className="btn btn-primary w-full" disabled={submitting}>
                        {submitting ? "Guardando..." : "Guardar"}
                    </button>
                ) : null}
            </form>
        </Modal>
    );
}
