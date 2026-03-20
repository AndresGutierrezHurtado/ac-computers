"use client";

import { useEffect, useRef, useState } from "react";
import ChatMessage from "@/atoms/ChatMessage";
import ChatComposer from "@/molecules/ChatComposer";
import { CloseIcon, RobotIcon } from "@/atoms/Icons";

const API_URL = process.env.NEXT_PUBLIC_API_URL;
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
    // Refs
    const endRef = useRef(null);
    const phaseTimerRef = useRef(null);
    const messagesContainerRef = useRef(null);

    // States
    const [messages, setMessages] = useState(DEFAULT_MESSAGES);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [loadingPhase, setLoadingPhase] = useState("thinking");
    const [hydrated, setHydrated] = useState(false);

    const scrollToBottom = () => {
        if (messagesContainerRef.current) {
            messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
        }
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages, loading]);

    useEffect(() => {
        if (!loading) {
            if (phaseTimerRef.current) {
                clearInterval(phaseTimerRef.current);
                phaseTimerRef.current = null;
            }
            return;
        }
        setLoadingPhase("thinking");
        phaseTimerRef.current = setInterval(() => {
            setLoadingPhase((p) => (p === "thinking" ? "catalog" : "thinking"));
        }, 2200);
        return () => {
            if (phaseTimerRef.current) {
                clearInterval(phaseTimerRef.current);
                phaseTimerRef.current = null;
            }
        };
    }, [loading]);

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
        if (!input.trim() || loading) return;

        const userMessage = input.trim();
        setInput("");

        const historyForApi = [...messages, { role: "user", content: userMessage }];
        setMessages(historyForApi);

        if (!API_URL) {
            setMessages((prev) => [
                ...prev,
                {
                    role: "assistant",
                    content: "La API no está configurada en este entorno.",
                    consultedProducts: undefined,
                },
            ]);
            return;
        }

        setLoading(true);
        try {
            const response = await fetch(`${API_URL}/products/sales-chat`, {
                method: "POST",
                headers: {
                    "content-type": "application/json",
                    accept: "application/json",
                },
                body: JSON.stringify({
                    messages: toApiMessages(historyForApi),
                }),
            });
            const json = await response.json();
            const data = json?.data;
            const reply =
                (typeof data?.message === "string" && data.message) ||
                json?.message ||
                "No pude generar una respuesta en este momento.";
            const consultedProducts = Array.isArray(data?.consultedProducts)
                ? data.consultedProducts
                : [];

            setMessages((prev) => [
                ...prev,
                {
                    role: "assistant",
                    content: reply,
                    consultedProducts: consultedProducts.length > 0 ? consultedProducts : undefined,
                },
            ]);
        } catch (error) {
            setMessages((prev) => [
                ...prev,
                {
                    role: "assistant",
                    content: "Ocurrió un error al consultar la IA. Intenta de nuevo.",
                    consultedProducts: undefined,
                },
            ]);
        } finally {
            setLoading(false);
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            if (!loading && input.trim()) {
                handleSubmit(event);
            }
        }
    };

    const closeDropdown = () => {
        if (document.activeElement instanceof HTMLElement) {
            document.activeElement.blur();
        }
    };

    return (
        <details className="dropdown dropdown-top dropdown-end fixed bottom-12 right-12 z-[999]">
            <summary className="btn btn-primary btn-circle shadow-lg mt-5">
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
                            onClick={closeDropdown}
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
                        {loading && (
                            <div className="chat chat-start">
                                <div className="chat-bubble chat-bubble-secondary text-sm">
                                    <div className="flex flex-col gap-0.5">
                                        <span className="inline-flex items-center gap-2">
                                            <span className="loading loading-dots loading-xs" />
                                            {loadingPhase === "thinking"
                                                ? "Pensando…"
                                                : "Buscando en el catálogo…"}
                                        </span>
                                        <span className="text-[0.7rem] text-base-content/60">
                                            {loadingPhase === "thinking"
                                                ? "Preparando la mejor respuesta."
                                                : "Consultando productos por similitud (embeddings)."}
                                        </span>
                                    </div>
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
                        loading={loading}
                    />
                </li>
            </ul>
        </details>
    );
}
