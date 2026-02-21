import SearchInput from "@/atoms/SearchInput";

export default function PageHeader({ title, searchPlaceholder, searchValue, searchOnChange }) {
    return (
        <div className="flex flex-col sm:flex-row items-start sm:items-end gap-4 justify-between">
            <h2 className="text-4xl font-extrabold tracking-tight">{title}</h2>
            <SearchInput placeholder={searchPlaceholder} value={searchValue} onChange={searchOnChange} />
        </div>
    );
}
