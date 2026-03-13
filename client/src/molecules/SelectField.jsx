import React from "react";
import Select from "@/atoms/Select";

export default function SelectField({
    label,
    name,
    value,
    defaultValue,
    onChange,
    options = [],
    disabled = false,
    placeholder,
    required = false,
    className = "",
}) {
    return (
        <div className="fieldset">
            <label className="fieldset-label">
                {label}
                {required ? (
                    <span className="text-red-500 ml-0.5" aria-hidden>
                        *
                    </span>
                ) : null}
            </label>
            <Select
                name={name}
                {...(value !== undefined ? { value } : { defaultValue })}
                onChange={onChange}
                options={options}
                disabled={disabled}
                placeholder={placeholder}
                required={required}
                className={className}
            />
        </div>
    );
}
