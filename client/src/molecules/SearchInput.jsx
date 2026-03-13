"use client";

import { useEffect, useState } from "react";
import { SearchIcon } from "../atoms/Icons";
import Input from "@/atoms/Input";

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
        <Input
            icon={<SearchIcon size={16} />}
            placeholder={placeholder}
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
        />
    );
}
