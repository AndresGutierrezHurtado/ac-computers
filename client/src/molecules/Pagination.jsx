"use client";
import { useRouter } from "next/navigation";
import React from "react";

export default function Pagination({ page, count, limit, url, query = "", onPageChange }) {
    const router = useRouter();

    const handleChangePage = (page) => {
        if (onPageChange) {
            onPageChange(page);
            return;
        }

        const params = new URLSearchParams(query);
        params.set("page", page);
        router.push(`${url}?${params.toString()}`);
    };

    return (
        <div className="join">
            <button
                className="join-item btn disabled:cursor-not-allowed"
                disabled={page === 1}
                onClick={() => handleChangePage(page - 1)}
            >
                «
            </button>
            <button className="join-item btn">página {page}</button>
            <button
                className="join-item btn disabled:cursor-not-allowed"
                disabled={page === Math.ceil(count / limit)}
                onClick={() => handleChangePage(page + 1)}
            >
                »
            </button>
        </div>
    );
}
