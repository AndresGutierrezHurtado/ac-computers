"use client";

import { useEffect, useMemo, useState } from "react";
import Swal from "sweetalert2";

import Modal from "@/molecules/Modal";
import AdminFormField from "@/molecules/AdminFormField";
import AdminSelectField from "@/molecules/AdminSelectField";
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
    const [imageFile, setImageFile] = useState(null);
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
            setImageFile(null);
            setSpecs([{ id: "", specificationId: "", value: "", specificationValueId: "" }]);
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

        setSpecs(mappedSpecs.length > 0 ? mappedSpecs : [{ id: "", specificationId: "", value: "", specificationValueId: "" }]);
        setImageFile(null);
    }, [open, product]);

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

        if (imageFile) {
            formData.append("image", imageFile);
        }

        specList.forEach((spec, index) => {
            if (spec.id) {
                formData.append(`specifications[${index}].id`, spec.id);
            }
            formData.append(`specifications[${index}].specificationId`, spec.specificationId);
            formData.append(`specifications[${index}].value`, spec.value);
            if (spec.specificationValueId) {
                formData.append(`specifications[${index}].specificationValueId`, spec.specificationValueId);
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

        if (mode === "create" && !imageFile) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Debes cargar una imagen principal",
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
                <AdminFormField
                    label="Nombre"
                    name="name"
                    placeholder="Nombre del producto"
                    value={form.name}
                    onChange={handleChange("name")}
                    disabled={readOnly}
                />
                <AdminFormField
                    label="Descripción"
                    name="description"
                    placeholder="Descripción del producto"
                    as="textarea"
                    value={form.description}
                    onChange={handleChange("description")}
                    disabled={readOnly}
                />
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <AdminFormField
                        label="Precio"
                        name="price"
                        type="number"
                        placeholder="Ej: 1500000"
                        value={form.price}
                        onChange={handleChange("price")}
                        disabled={readOnly}
                    />
                    <AdminFormField
                        label="Descuento"
                        name="discount"
                        type="number"
                        placeholder="0"
                        value={form.discount}
                        onChange={handleChange("discount")}
                        disabled={readOnly}
                    />
                </div>
                <AdminSelectField
                    label="Condición"
                    name="condition"
                    value={form.condition}
                    onChange={handleChange("condition")}
                    options={CONDITION_OPTIONS}
                    disabled={readOnly}
                />
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <AdminSelectField
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
                        required={false}
                    />
                    <AdminSelectField
                        label="Subcategoría"
                        name="subCategoryId"
                        value={form.subCategoryId}
                        onChange={handleChange("subCategoryId")}
                        options={subCategoryOptions}
                        disabled={readOnly}
                    />
                </div>
                <AdminSelectField
                    label="Marca"
                    name="brandId"
                    value={form.brandId}
                    onChange={handleChange("brandId")}
                    options={brandOptions}
                    disabled={readOnly}
                />
                <fieldset className="fieldset">
                    <label className="label">
                        <span className="label-text font-semibold">
                            Imagen principal{mode === "create" ? " *" : ""}
                        </span>
                    </label>
                    <input
                        type="file"
                        className="file-input file-input-bordered file-input-sm w-full"
                        onChange={(event) => setImageFile(event.target.files?.[0] || null)}
                        disabled={readOnly}
                        accept="image/*"
                    />
                </fieldset>
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
