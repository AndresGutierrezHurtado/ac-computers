"use client";

import { useEffect, useMemo, useState } from "react";

export default function FormattedNumberInput({
    value,
    onChange,
    placeholder,
    name,
    disabled = false,
    dense = true,
}) {
    const formatter = useMemo(
        () =>
            new Intl.NumberFormat("es-CO", {
                maximumFractionDigits: 0,
            }),
        [],
    );

    const formatValue = (raw) => {
        if (raw === null || raw === undefined || raw === "") return "";
        const numeric = Number(raw);
        if (Number.isNaN(numeric)) return "";
        return formatter.format(numeric);
    };

    const [displayValue, setDisplayValue] = useState(formatValue(value));

    useEffect(() => {
        setDisplayValue(formatValue(value));
    }, [value]);

    const handleChange = (event) => {
        if (disabled) return;
        const raw = event.target.value.replace(/[^\d]/g, "");
        setDisplayValue(formatValue(raw));
        onChange?.(raw);
    };

    return (
        <label
            className={`input input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full disabled:input-bordered ${dense ? "input-sm" : ""} ${disabled ? "pointer-events-none opacity-60" : ""}`}
        >
            <input
                type="text"
                inputMode="numeric"
                name={name}
                placeholder={placeholder}
                value={displayValue}
                onChange={handleChange}
                disabled={disabled}
                className="grow bg-transparent focus:outline-0"
            />
        </label>
    );
}
