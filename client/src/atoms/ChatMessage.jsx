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
    h1: ({ children }) => <h1 className="text-lg font-bold mt-3 mb-2">{children}</h1>,
    h2: ({ children }) => <h2 className="text-base font-semibold mt-3 mb-2">{children}</h2>,
    h3: ({ children }) => <h3 className="text-sm font-semibold mt-2 mb-1">{children}</h3>,

    p: ({ children }) => <p className="mb-2 leading-relaxed">{children}</p>,

    ul: ({ children }) => <ul className="list-disc pl-5 mb-2 space-y-1">{children}</ul>,
    ol: ({ children }) => <ol className="list-decimal pl-5 mb-2 space-y-1">{children}</ol>,
    li: ({ children }) => <li className="leading-snug">{children}</li>,

    strong: ({ children }) => <strong className="font-semibold">{children}</strong>,
    em: ({ children }) => <em className="italic">{children}</em>,

    blockquote: ({ children }) => (
        <blockquote className="border-l-4 border-base-300 pl-3 italic opacity-80 my-2">
            {children}
        </blockquote>
    ),

    code({ inline, children }) {
        return inline ? (
            <code className="px-1 py-0.5 rounded bg-base-300/40 text-xs">
                {children}
            </code>
        ) : (
            <pre className="bg-base-300/40 rounded p-2 overflow-x-auto text-xs my-2">
                <code>{children}</code>
            </pre>
        );
    },

    a: ({ href, children }) => (
        <a href={href} target="_blank" className="text-primary underline">
            {children}
        </a>
    ),

    hr: () => <hr className="my-3 border-base-300" />,

    // TABLAS (lo que ya tenías mejorado)
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
                            <div className="max-w-none min-w-0 text-sm leading-relaxed">
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
