"use client";

import AdminSearchInput from "@/molecules/AdminSearchInput";

export default function AdminTableCard({
    title,
    searchPlaceholder,
    searchValue,
    onSearchChange,
    columns = [],
    emptyMessage = "No hay registros...",
    loading = false,
    children,
}) {
    return (
        <div className="card bg-zinc-950/30 rounded [&_p]:grow-0">
            <div className="card-body p-4">
                <div className="flex flex-col sm:flex-row gap-4 justify-between items-center w-full">
                    <h2 className="text-3xl font-bold">{title}</h2>
                    {searchPlaceholder && onSearchChange ? (
                        <AdminSearchInput
                            placeholder={searchPlaceholder}
                            value={searchValue}
                            onChange={onSearchChange}
                        />
                    ) : null}
                </div>
            </div>
            <div className="w-full overflow-x-auto">
                <table className="w-full table rounded">
                    <thead className="transparent bg-zinc-950/30">
                        <tr className="text-[15px] [&>*]:py-3">
                            {columns.map((column) => (
                                <th key={column}>{column}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr className="[&>*]:py-4 text-center text-xl">
                                <td colSpan={columns.length}>Cargando...</td>
                            </tr>
                        ) : children ? (
                            children
                        ) : (
                            <tr className="[&>*]:py-4 text-center text-xl">
                                <td colSpan={columns.length}>{emptyMessage}</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
