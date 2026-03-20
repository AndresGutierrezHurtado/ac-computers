"use client";

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

export default function ChatMessage({ role, content, consultedProducts }) {
    const isUser = role === "user";
    const hasHits = Array.isArray(consultedProducts) && consultedProducts.length > 0;

    return (
        <div className={`chat ${isUser ? "chat-end" : "chat-start"}`}>
            <div
                className={`chat-bubble text-sm ${isUser ? "chat-bubble-primary" : "chat-bubble-secondary"}`}
            >
                {isUser ? (
                    content
                ) : (
                    <>
                        <div className="prose prose-sm max-w-none prose-headings:mb-2 prose-p:mb-1 prose-ul:mb-1 prose-ol:mb-1 prose-li:marker:text-current">
                            <ReactMarkdown remarkPlugins={[remarkGfm]}>
                                {String(content ?? "")}
                            </ReactMarkdown>
                        </div>
                        {hasHits && (
                            <div className="mt-3 border-t border-base-content/10 pt-2 text-left">
                                <p className="mb-1.5 text-[0.7rem] font-semibold uppercase tracking-wide text-base-content/70">
                                    Productos consultados en el catálogo
                                </p>
                                <ul className="space-y-2">
                                    {consultedProducts.map((p) => (
                                        <li
                                            key={p.id}
                                            className="rounded-md bg-base-200/90 px-2 py-1.5 text-[0.75rem] leading-snug"
                                        >
                                            <span className="font-medium text-base-content">{p.name}</span>
                                            <span className="mx-1 text-base-content/40">·</span>
                                            <span className="text-primary">{formatCop(p.price)}</span>
                                            {p.brand ? (
                                                <span className="mt-0.5 block text-[0.7rem] text-base-content/70">
                                                    {p.brand}
                                                    {p.condition ? ` · ${p.condition}` : ""}
                                                </span>
                                            ) : null}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}
