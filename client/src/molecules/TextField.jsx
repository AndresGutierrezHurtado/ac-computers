import React from "react";
import Input from "@/atoms/Input";

export default function TextField({
    label,
    placeholder,
    name,
    value,
    defaultValue,
    onChange,
    disabled = false,
    type = "text",
    autoComplete,
    required = false,
    className = "",
}) {
    return (
        <div className="fieldset">
            <label className="fieldset-label text-sm">
                {label}
                {required ? (
                    <span className="text-red-500 ml-0.5" aria-hidden>
                        *
                    </span>
                ) : null}
            </label>
            <Input
                type={type}
                name={name}
                autoComplete={autoComplete}
                placeholder={placeholder}
                required={required}
                {...(value !== undefined ? { value } : { defaultValue })}
                onChange={onChange}
                disabled={disabled}
                className={className}
            />
        </div>
    );
}
