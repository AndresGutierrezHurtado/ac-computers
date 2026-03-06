"use client";

export default function AdminSelectField({
    label,
    name,
    value,
    onChange,
    options = [],
    placeholder = "Selecciona",
    disabled = false,
    required = true,
}) {
    const labelClass =
        "label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5";

    return (
        <fieldset className="fieldset">
            <label className="label">
                <span className={required ? labelClass : "label-text font-semibold"}>
                    {label}
                </span>
            </label>
            <select
                name={name}
                value={value}
                onChange={onChange}
                className="select select-sm select-bordered focus:select-primary focus:outline-0 w-full"
                disabled={disabled}
            >
                <option value="">{placeholder}</option>
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </fieldset>
    );
}
