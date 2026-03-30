"use client";

import { useEffect, useRef, useState } from "react";
import ChatMessage from "@/atoms/ChatMessage";
import ChatComposer from "@/molecules/ChatComposer";
import { CloseIcon, RobotIcon } from "@/atoms/Icons";
import { createStreamer } from "@/hooks/useClientData";

const STORAGE_KEY_MESSAGES = "ac_ai_chat_messages_v2";

const DEFAULT_MESSAGES = [
    {
        role: "assistant",
        content: "Hola, soy el asesor de AC Computers. ¿Qué estás buscando?",
        consultedProducts: undefined,
    },
];

function toApiMessages(list) {
    return list.map((m) => ({
        role: m.role === "user" ? "USER" : m.role === "assistant" ? "ASSISTANT" : "SYSTEM",
        content: m.content,
    }));
}

export default function AIChatWidget() {
    const streamer = createStreamer();

    // Refs
    const endRef = useRef(null);
    const messagesContainerRef = useRef(null);

    // States
    const [messages, setMessages] = useState(DEFAULT_MESSAGES);
    const [input, setInput] = useState("");
    const [status, setStatus] = useState("idle");
    const [hydrated, setHydrated] = useState(false);
    const [dropdownOpen, setDropdownOpen] = useState(false);

    const scrollToBottom = () => {
        if (messagesContainerRef.current) {
            messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
        }
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages, status]);

    useEffect(() => {
        try {
            const storedMessages = localStorage.getItem(STORAGE_KEY_MESSAGES);
            if (storedMessages) {
                const parsedMessages = JSON.parse(storedMessages);
                if (Array.isArray(parsedMessages) && parsedMessages.length > 0) {
                    setMessages(parsedMessages);
                }
            }
        } catch (error) {
            // If storage is corrupted, fall back to defaults.
        }
        setHydrated(true);
    }, []);

    useEffect(() => {
        if (!hydrated) return;
        try {
            localStorage.setItem(STORAGE_KEY_MESSAGES, JSON.stringify(messages));
        } catch (error) {
            // ignore storage errors
        }
    }, [hydrated, messages]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!input.trim() || status !== "idle") return;

        const userMessage = input.trim();
        setInput("");

        const historyForApi = [...messages, { role: "user", content: userMessage }];
        setMessages(historyForApi);

        setStatus("loading");
        streamer.stream(
            "POST",
            "/products/sales-chat",
            {
                messages: toApiMessages(historyForApi),
            },
            {
                onChunk: (payload) => {
                    const parsedPayload =
                        typeof payload === "string" ? JSON.parse(payload) : payload;
                    const chunk = parsedPayload?.data?.message;
                    const consultedProducts = parsedPayload?.data?.consultedProducts;

                    if (!chunk) return;

                    setStatus("streaming");
                    setMessages((prev) => {
                        if (!prev.length) return prev;
                        const next = [...prev];
                        const lastIndex = next.length - 1;
                        const last = next[lastIndex];

                        if (last?.role !== "assistant") {
                            next.push({ role: "assistant", content: chunk, consultedProducts });
                            return next;
                        }

                        next[lastIndex] = {
                            ...last,
                            content: `${last.content || ""}${chunk}`,
                            consultedProducts,
                        };

                        return next;
                    });
                },
                onComplete: () => {
                    setStatus("idle");
                },
                onError: (error) => {
                    const message = error?.message;
                    setStatus("idle");

                    setMessages((prev) => {
                        if (!prev.length) {
                            return [
                                {
                                    role: "assistant",
                                    content: message,
                                    consultedProducts: undefined,
                                },
                            ];
                        }

                        const next = [...prev];
                        const lastIndex = next.length - 1;
                        const last = next[lastIndex];

                        next[lastIndex] = {
                            ...last,
                            content: message,
                            consultedProducts: undefined,
                        };

                        return next;
                    });
                },
            },
        );
    };

    const handleKeyDown = (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            if (status === "idle" && input.trim()) {
                handleSubmit(event);
            }
        }
    };

    return (
        <details
            open={dropdownOpen}
            className="dropdown dropdown-top dropdown-end fixed bottom-12 right-12 z-[999]"
        >
            <summary
                className="btn btn-primary btn-circle shadow-lg mt-5"
                onClick={() => setDropdownOpen(true)}
            >
                <RobotIcon size={20} />
            </summary>
            <ul className="dropdown-content bg-base-100/95 border border-base-200 shadow-xl backdrop-blur w-[min(92vw,380px)] h-[min(70vh,520px)] rounded-lg p-0 flex flex-col">
                <li className="list-none flex min-h-0 flex-1 flex-col gap-3 p-5">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-2 text-base font-semibold">
                            <RobotIcon size={18} />
                            Asesor IA
                        </div>
                        <button
                            type="button"
                            className="btn btn-ghost btn-sm btn-circle"
                            onClick={() => setDropdownOpen(false)}
                        >
                            <CloseIcon size={18} />
                        </button>
                    </div>

                    <div
                        className="min-h-0 flex-1 space-y-3 overflow-y-auto pr-1"
                        ref={messagesContainerRef}
                    >
                        {messages.map((message, index) => (
                            <ChatMessage
                                key={`${message.role}-${index}-${message.content?.slice?.(0, 12) ?? ""}`}
                                role={message.role}
                                content={message.content}
                                consultedProducts={message.consultedProducts}
                            />
                        ))}
                        {status === "loading" && (
                            <div className="chat chat-start">
                                <div className="chat-bubble chat-bubble-secondary text-sm">
                                    <span className="animate-pulse">Pensando ...</span>
                                </div>
                            </div>
                        )}
                        <div ref={endRef} />
                    </div>

                    <ChatComposer
                        value={input}
                        onChange={(event) => setInput(event.target.value)}
                        onSubmit={handleSubmit}
                        onKeyDown={handleKeyDown}
                        loading={status === "loading"}
                    />
                </li>
            </ul>
        </details>
    );
}
