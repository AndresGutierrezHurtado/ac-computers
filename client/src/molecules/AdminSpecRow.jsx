"use client";

export default function AdminSpecRow({
    index,
    spec,
    onChange,
    onRemove,
    disabled = false,
}) {
    const handleChange = (field) => (event) => {
        onChange(index, { ...spec, [field]: event.target.value });
    };

    return (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-3 items-end">
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Spec ID</span>
                </label>
                <input
                    className="input input-sm input-bordered focus:input-primary focus:outline-0"
                    placeholder="Ej: 1"
                    value={spec.specificationId}
                    onChange={handleChange("specificationId")}
                    disabled={disabled}
                />
            </fieldset>
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Valor</span>
                </label>
                <input
                    className="input input-sm input-bordered focus:input-primary focus:outline-0"
                    placeholder="Ej: 16"
                    value={spec.value}
                    onChange={handleChange("value")}
                    disabled={disabled}
                />
            </fieldset>
            <fieldset className="fieldset">
                <label className="label">
                    <span className="label-text font-semibold">Spec Value ID</span>
                </label>
                <input
                    className="input input-sm input-bordered focus:input-primary focus:outline-0"
                    placeholder="Opcional"
                    value={spec.specificationValueId}
                    onChange={handleChange("specificationValueId")}
                    disabled={disabled}
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
