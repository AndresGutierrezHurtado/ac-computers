"use client";

import { useEffect, useMemo } from "react";

import { useGetData } from "@/hooks/useClientData";

export default function AdminSpecRow({
    index,
    spec,
    specifications = [],
    onChange,
    onRemove,
    disabled = false,
}) {
    const selectedSpec = useMemo(
        () =>
            specifications.find((item) => item.id?.toString() === spec.specificationId?.toString()),
        [specifications, spec.specificationId],
    );

    const specType = selectedSpec?.type || "text";

    const valuesEndpoint =
        specType === "select" && selectedSpec?.id
            ? `/specification-values?specificationId=${selectedSpec.id}`
            : null;

    const { data: specValues } = useGetData(valuesEndpoint);

    const handleSpecChange = (event) => {
        const nextId = event.target.value;
        onChange(index, {
            ...spec,
            specificationId: nextId,
            value: "",
            specificationValueId: "",
        });
    };

    const handleValueChange = (event) => {
        onChange(index, { ...spec, value: event.target.value, specificationValueId: "" });
    };

    const handleSelectValueChange = (event) => {
        const nextId = event.target.value;
        const match = (specValues || []).find((item) => item.id?.toString() === nextId);
        onChange(index, {
            ...spec,
            value: match?.value || "",
            specificationValueId: nextId,
        });
    };

    useEffect(() => {
        if (specType !== "select") return;
        if (!spec.value || spec.specificationValueId) return;
        const match = (specValues || []).find((item) => item.value === spec.value);
        if (!match) return;
        onChange(index, {
            ...spec,
            value: match.value,
            specificationValueId: match.id?.toString() || "",
        });
    }, [specType, spec.value, spec.specificationValueId, specValues, onChange, index, spec]);

    return (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-3 items-end">
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Especificación</span>
                </label>
                <select
                    className="select select-sm select-bordered focus:select-primary focus:outline-0"
                    value={spec.specificationId}
                    onChange={handleSpecChange}
                    disabled={disabled}
                >
                    <option value="">Selecciona una especificación</option>
                    {specifications.map((item) => (
                        <option key={item.id} value={item.id}>
                            {item.name}
                        </option>
                    ))}
                </select>
            </fieldset>
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Valor</span>
                </label>
                {specType === "select" ? (
                    <select
                        className="select select-sm select-bordered focus:select-primary focus:outline-0"
                        value={spec.specificationValueId}
                        onChange={handleSelectValueChange}
                        disabled={disabled || !selectedSpec}
                    >
                        <option value="">Selecciona un valor</option>
                        {(specValues || []).map((value) => (
                            <option key={value.id} value={value.id}>
                                {value.value}
                            </option>
                        ))}
                    </select>
                ) : (
                    <input
                        className="input input-sm input-bordered focus:input-primary focus:outline-0"
                        placeholder={selectedSpec?.unit ? `Ej: 16 ${selectedSpec.unit}` : "Ej: 16"}
                        value={spec.value}
                        onChange={handleValueChange}
                        disabled={disabled || !selectedSpec}
                        type={specType === "number" ? "number" : "text"}
                    />
                )}
            </fieldset>
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Tipo</span>
                </label>
                <input
                    className="input input-sm input-bordered focus:input-primary focus:outline-0"
                    value={selectedSpec?.type || "—"}
                    disabled
                />
            </fieldset>
            <button
                type="button"
                className="btn btn-sm btn-ghost text-red-400"
                onClick={() => onRemove(index)}
                disabled={disabled}
            >
                Quitar
            </button>
        </div>
    );
}
