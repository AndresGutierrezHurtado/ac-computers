"use client";

import Link from "next/link";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

function formatCop(value) {
    if (value == null || Number.isNaN(Number(value))) return "—";
    try {
        return new Intl.NumberFormat("es-CO", {
            style: "currency",
            currency: "COP",
            maximumFractionDigits: 0,
        }).format(Number(value));
    } catch {
        return String(value);
    }
}

/** Tablas GFM: líneas visibles + scroll horizontal si sobrepasan el bubble del chat */
const markdownComponents = {
    table({ children }) {
        return (
            <div className="my-3 w-full max-w-full overflow-x-auto overscroll-x-contain rounded border border-base-200/10">
                <table className="table table-xs">
                    {children}
                </table>
            </div>
        );
    },
    thead({ children }) {
        return <thead className="bg-base-200/10">{children}</thead>;
    },
    th({ children, ...props }) {
        return (
            <th
                className="px-2 py-1.5 text-left font-semibold align-top"
                {...props}
            >
                {children}
            </th>
        );
    },
    td({ children, ...props }) {
        return (
            <td className="px-2 py-1.5 align-top" {...props}>
                {children}
            </td>
        );
    },
};

export default function ChatMessage({ role, content, consultedProducts }) {
    const isUser = role === "user";
    const hasHits = Array.isArray(consultedProducts) && consultedProducts.length > 0;

    return (
        <>
            <div className={`chat ${isUser ? "chat-end" : "chat-start"}`}>
                <div
                    className={`chat-bubble text-sm ${isUser ? "chat-bubble-primary" : "chat-bubble-secondary"}`}
                >
                    {isUser ? (
                        <span>{content}</span>
                    ) : (
                        <div className="prose prose-sm max-w-none min-w-0 prose-headings:mb-2 prose-p:mb-1 prose-ul:mb-1 prose-ol:mb-1 prose-li:marker:text-current">
                            <ReactMarkdown remarkPlugins={[remarkGfm]} components={markdownComponents}>
                                {typeof content === "string" ? content : ""}
                            </ReactMarkdown>
                        </div>
                    )}
                </div>
            </div>
            {hasHits && (
                <div className="chat chat-start">
                    <div className="w-full grid grid-cols-2 gap-2">
                        {consultedProducts.map((p) => (
                            <Link
                                key={p.id}
                                className="rounded-md bg-white/10 hover:bg-white/30 duration-200 px-2 py-1.5 text-[0.75rem] leading-snug"
                                href={`/products/${p.id}`}
                            >
                                <p className="font-medium text-base-content">{p.name}</p>
                                <div className="flex items-center justify-between">
                                    {p.brand ? (
                                        <span className="mt-0.5 block text-[0.7rem] text-base-content/70">
                                            {p.brand}
                                            {p.condition ? ` · ${p.condition}` : ""}
                                        </span>
                                    ) : (
                                        <span />
                                    )}
                                    {"price" in p && p.price != null && (
                                        <span className="ml-2 font-semibold text-xs text-primary">
                                            {formatCop(p.price)}
                                        </span>
                                    )}
                                </div>
                            </Link>
                        ))}
                    </div>
                </div>
            )}
        </>
    );
}
