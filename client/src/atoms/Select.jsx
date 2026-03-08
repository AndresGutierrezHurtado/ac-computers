"use client";

export default function Select({
    value,
    onChange,
    options = [],
    name,
    disabled = false,
    placeholder,
    "aria-label": ariaLabel,
}) {
    return (
        <select
            name={name}
            value={value}
            onChange={onChange}
            disabled={disabled}
            aria-label={ariaLabel}
            className="select select-bordered select-sm focus:select-primary focus:outline-0 focus-within:outline-0 focus-within:input-primary w-full"
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
