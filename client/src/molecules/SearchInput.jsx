"use client";

import { useEffect, useState } from "react";
import { SearchIcon } from "../atoms/icons";

export default function SearchInput({ placeholder, value, onChange }) {
    const [inputValue, setInputValue] = useState(value);

    // UseEffects for improve performance by searching when the user stops typing
    useEffect(() => {
        setInputValue(value);
    }, [value]);

    useEffect(() => {
        const timeout = setTimeout(() => {
            onChange(inputValue);
        }, 500);
        return () => clearTimeout(timeout);
    }, [inputValue, onChange]);

    return (
        <label className="input input-sm input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2">
            <SearchIcon size={16} />
            <input
                type="search"
                placeholder={placeholder}
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
            />
        </label>
    );
}
