import React from "react";

export default function Textarea({
    placeholder,
    value,
    defaultValue,
    onChange,
    name,
    disabled = false,
    required = false,
    resizable = true,
    className = "",
    rows,
}) {
    const isControlled = value !== undefined;
    const base = "textarea focus-within:outline-0 focus-within:textarea-primary w-full";
    return (
        <textarea
            name={name}
            className={`${base} ${className}`.trim()}
            placeholder={placeholder}
            disabled={disabled}
            required={required}
            rows={rows}
            style={{ resize: resizable ? "vertical" : "none" }}
            {...(isControlled ? { value, onChange } : { defaultValue, onChange })}
        />
    );
}
