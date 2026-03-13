import React from "react";
import Textarea from "@/atoms/Textarea";

export default function TextareaField({
    label,
    placeholder,
    name,
    value,
    defaultValue,
    onChange,
    disabled = false,
    resizable = true,
    rows,
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
            <Textarea
                name={name}
                placeholder={placeholder}
                required={required}
                {...(value !== undefined ? { value } : { defaultValue })}
                onChange={onChange}
                disabled={disabled}
                resizable={resizable}
                rows={rows}
                className={className}
            />
        </div>
    );
}
