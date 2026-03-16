"use client";

import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

export default function ChatMessage({ role, content }) {
    const isUser = role === "user";

    return (
        <div className={`chat ${isUser ? "chat-end" : "chat-start"}`}>
            <div
                className={`chat-bubble text-sm ${isUser ? "chat-bubble-primary" : "chat-bubble-secondary"}`}
            >
                {isUser ? (
                    content
                ) : (
                    <div className="prose prose-sm max-w-none prose-headings:mb-2 prose-p:mb-1 prose-ul:mb-1 prose-ol:mb-1 prose-li:marker:text-current">
                        <ReactMarkdown remarkPlugins={[remarkGfm]}>
                            {String(content ?? "")}
                        </ReactMarkdown>
                    </div>
                )}
            </div>
        </div>
    );
}
