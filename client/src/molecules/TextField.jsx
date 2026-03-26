import React from "react";
import Input from "@/atoms/Input";

export default function TextField({
    id,
    name,
    value,
    defaultValue,
    onChange,
    label,
    placeholder,
    autoComplete,
    type,
    className,
    disabled,
    required,
    togglePassword,
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
                id={id}
                type={type}
                name={name}
                autoComplete={autoComplete}
                placeholder={placeholder}
                required={required}
                {...(value !== undefined ? { value } : { defaultValue })}
                onChange={onChange}
                disabled={disabled}
                togglePassword={togglePassword}
                className={className}
            />
        </div>
    );
}
