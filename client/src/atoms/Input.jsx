import React, { useState } from "react";
import { EyeIcon, EyeSlashIcon } from "./Icons";

export default function Input({
    id,
    name,
    value,
    onChange,
    placeholder,
    children,
    icon = null,
    type = "text",
    className = "",
    togglePassword = false,
    autoComplete = "off",
    disabled = false,
    required = false,
}) {
    const [showPassword, setShowPassword] = useState(false);

    const isControlled = value !== undefined;
    const base =
        "input focus-within:outline-0 focus-within:input-primary w-full disabled:input-bordered";

    return (
        <label className={`${base} ${className}`.trim()}>
            {icon && <span className="icon">{icon}</span>}
            <input
                id={id}
                type={showPassword ? "text" : type}
                name={name}
                autoComplete={autoComplete}
                className="grow"
                placeholder={placeholder}
                disabled={disabled}
                required={required}
                {...(isControlled ? { value, onChange } : { onChange })}
            />
            {togglePassword && (
                <button
                    type="button"
                    className="btn btn-ghost w-7 h-7 p-0"
                    onClick={() => setShowPassword(!showPassword)}
                >
                    {showPassword ? <EyeIcon size={16} /> : <EyeSlashIcon size={16} />}
                </button>
            )}
            {children}
        </label>
    );
}
