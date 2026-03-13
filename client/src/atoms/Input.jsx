import React from "react";

export default function Input({
    placeholder,
    value,
    defaultValue,
    onChange,
    name,
    autoComplete,
    icon,
    children,
    disabled = false,
    required = false,
    type = "text",
    className = "",
}) {
    const isControlled = value !== undefined;
    const base =
        "input focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full disabled:input-bordered";
    return (
        <label className={`${base} ${className}`.trim()}>
            {icon && <span className="icon">{icon}</span>}
            <input
                type={type}
                name={name}
                autoComplete={autoComplete}
                className="grow"
                placeholder={placeholder}
                disabled={disabled}
                required={required}
                {...(isControlled ? { value, onChange } : { defaultValue, onChange })}
            />
            {children}
        </label>
    );
}
