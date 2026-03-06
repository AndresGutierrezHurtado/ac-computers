"use client";

import SearchInput from "@/atoms/SearchInput";

export default function AdminSearchInput({ placeholder, value = "", onChange = () => {} }) {
    return (
        <div className="w-full max-w-sm">
            <SearchInput placeholder={placeholder} value={value} onChange={onChange} />
        </div>
    );
}
