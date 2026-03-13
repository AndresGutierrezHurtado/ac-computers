"use client";
import { useRouter } from "next/navigation";
import React, { useMemo } from "react";
import { ChevronLeftIcon, ChevronRightIcon } from "@/atoms/Icons";
import Select from "@/atoms/Select";

const DEFAULT_LIMIT_OPTIONS = [5, 10, 15, 20, 25, 30, 50];

export default function Pagination({
    page,
    count,
    limit,
    url,
    query = "",
    onPageChange,
    onLimitChange,
    limitOptions = DEFAULT_LIMIT_OPTIONS,
}) {
    const router = useRouter();

    const resolvedLimitOptions = useMemo(() => {
        const set = new Set(limitOptions);
        if (limit != null && limit !== "") set.add(Number(limit));
        return Array.from(set)
            .filter((n) => Number.isFinite(n) && n > 0)
            .sort((a, b) => a - b);
    }, [limit, limitOptions]);

    const handleChangePage = (page) => {
        if (onPageChange) {
            onPageChange(page);
            return;
        }

        const params = new URLSearchParams(query);
        params.set("page", page);
        router.push(`${url}?${params.toString()}`);
    };

    const handleLimitChange = (event) => {
        const next = Number(event.target.value);
        if (Number.isNaN(next) || next < 1) return;

        if (onLimitChange) {
            onLimitChange(next);
            return;
        }

        if (url) {
            const params = new URLSearchParams(query);
            params.set("perPage", String(next));
            params.set("page", "1");
            router.push(`${url}?${params.toString()}`);
        }
    };

    const showLimitSelect = Boolean(onLimitChange || url);

    const totalPages = Math.ceil(count / limit);

    const items = useMemo(() => {
        if (totalPages < 1) return [];
        if (totalPages <= 5) {
            return Array.from({ length: totalPages }, (_, i) => i + 1);
        }

        const lastWindowStart = totalPages - 2;

        if (page <= 3) {
            return [1, 2, 3, "ellipsis", totalPages];
        }
        if (page >= lastWindowStart) {
            return [1, "ellipsis", totalPages - 2, totalPages - 1, totalPages];
        }
        return [1, "ellipsis", page, "ellipsis", totalPages];
    }, [page, totalPages]);

    return (
        <div className="w-full flex justify-between flex-wrap items-center gap-3">
            {showLimitSelect ? (
                <Select
                    name="perPage"
                    value={String(limit)}
                    onChange={handleLimitChange}
                    options={resolvedLimitOptions.map((n) => ({
                        value: String(n),
                        label: `${n} Por página`,
                    }))}
                    className="select-sm select-bordered w-auto max-w-[14rem]"
                />
            ) : null}

            <div className="flex items-center gap-2">
                <button
                    className="btn p-0 w-9 h-9 disabled:cursor-not-allowed"
                    disabled={page === 1}
                    onClick={() => handleChangePage(page - 1)}
                >
                    <ChevronLeftIcon size={16} />
                </button>
                {items.map((item, idx) =>
                    item === "ellipsis" ? (
                        <span
                            key={`ellipsis-${idx}`}
                            className="btn p-0 w-9 h-9 pointer-events-none cursor-default"
                            aria-hidden
                        >
                            ...
                        </span>
                    ) : (
                        <button
                            key={item}
                            type="button"
                            className={`btn p-0 w-9 h-9 ${item === page ? "bg-primary text-primary-content" : ""}`}
                            onClick={() => handleChangePage(item)}
                        >
                            {item}
                        </button>
                    )
                )}
                <button
                    className="btn p-0 w-9 h-9 disabled:cursor-not-allowed"
                    disabled={page === totalPages || totalPages < 1}
                    onClick={() => handleChangePage(page + 1)}
                >
                    <ChevronRightIcon size={16} />
                </button>
            </div>
        </div>
    );
}
