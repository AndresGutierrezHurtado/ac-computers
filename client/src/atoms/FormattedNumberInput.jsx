"use client";

import { useEffect, useMemo, useState } from "react";

export default function FormattedNumberInput({ value, onChange, placeholder }) {
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
        const raw = event.target.value.replace(/[^\d]/g, "");
        setDisplayValue(formatValue(raw));
        onChange(raw);
    };

    return (
        <label className="input input-sm input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full">
            <input
                type="text"
                inputMode="numeric"
                placeholder={placeholder}
                value={displayValue}
                onChange={handleChange}
                className="grow"
            />
        </label>
    );
}
