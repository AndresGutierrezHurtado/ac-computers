"use client";

export default function Select({
    value,
    defaultValue,
    onChange,
    options = [],
    name,
    disabled = false,
    required = false,
    placeholder,
    className = "",
    "aria-label": ariaLabel,
}) {
    const isControlled = value !== undefined;
    const base =
        "select select-bordered select-sm focus:select-primary focus:outline-0 focus-within:outline-0 focus-within:input-primary w-full";
    return (
        <select
            name={name}
            disabled={disabled}
            required={required}
            aria-label={ariaLabel}
            className={`${base} ${className}`.trim()}
            {...(isControlled ? { value, onChange } : { defaultValue, onChange })}
        >
            {placeholder != null ? (
                <option value="">{placeholder}</option>
            ) : null}
            {options.map((opt) => {
                const item = typeof opt === "object" ? opt : { value: opt, label: opt };
                return (
                    <option key={item.value} value={item.value}>
                        {item.label}
                    </option>
                );
            })}
        </select>
    );
}
