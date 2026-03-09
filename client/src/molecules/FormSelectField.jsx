"use client";

import Select from "@/atoms/Select";

export default function FormSelectField({
    label,
    name,
    value,
    onChange,
    options = [],
    placeholder,
    disabled = false,
    required = false,
    inline = false,
}) {
    const labelClass = required
        ? "label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5"
        : "label-text font-semibold";

    return (
        <fieldset className={`fieldset w-full max-w-50 ${inline ? "flex flex-row items-center gap-2 p-0" : ""}`}>
            <label className={`label ${inline ? "py-0 min-w-0" : ""}`}>
                <span className={labelClass}>{label}</span>
            </label>
            <Select
                name={name}
                value={value}
                onChange={onChange}
                options={options}
                placeholder={placeholder}
                disabled={disabled}
            />
        </fieldset>
    );
}
