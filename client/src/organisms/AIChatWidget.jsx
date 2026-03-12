"use client";

import { useEffect, useRef, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import { CloseIcon, RobotIcon } from "@/atoms/Icons";

const API_URL = process.env.NEXT_PUBLIC_API_URL;
const STORAGE_KEY_MESSAGES = "ac_ai_chat_messages_v1";
const STORAGE_KEY_OPEN = "ac_ai_chat_open_v1";

const DEFAULT_MESSAGES = [
    {
        role: "assistant",
        content: "Hola, soy el asistente de AC Computers. ¿Qué estás buscando?",
    },
];

export default function AIChatWidget() {
    const [open, setOpen] = useState(false);
    const [messages, setMessages] = useState(DEFAULT_MESSAGES);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [hydrated, setHydrated] = useState(false);
    const endRef = useRef(null);

    const scrollToBottom = () => {
        endRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        if (open) {
            scrollToBottom();
        }
    }, [open, messages]);

    useEffect(() => {
        try {
            const storedMessages = localStorage.getItem(STORAGE_KEY_MESSAGES);
            if (storedMessages) {
                const parsedMessages = JSON.parse(storedMessages);
                if (Array.isArray(parsedMessages) && parsedMessages.length > 0) {
                    setMessages(parsedMessages);
                }
            }

            const storedOpen = localStorage.getItem(STORAGE_KEY_OPEN);
            if (storedOpen != null) {
                setOpen(storedOpen === "true");
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

    useEffect(() => {
        if (!hydrated) return;
        try {
            localStorage.setItem(STORAGE_KEY_OPEN, String(open));
        } catch (error) {
            // ignore storage errors
        }
    }, [hydrated, open]);

    const appendMessage = (message) => {
        setMessages((prev) => [...prev, message]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!input.trim() || loading) return;

        const userMessage = input.trim();
        setInput("");
        appendMessage({ role: "user", content: userMessage });

        if (!API_URL) {
            appendMessage({
                role: "assistant",
                content: "La API no está configurada en este entorno.",
            });
            return;
        }

        setLoading(true);
        try {
            const response = await fetch(`${API_URL}/products/recommendations`, {
                method: "POST",
                headers: {
                    "content-type": "application/json",
                    accept: "application/json",
                },
                body: JSON.stringify(userMessage),
            });
            const json = await response.json();
            const reply =
                json?.data || json?.message || "No pude generar una respuesta en este momento.";

            appendMessage({ role: "assistant", content: reply });
        } catch (error) {
            appendMessage({
                role: "assistant",
                content: "Ocurrió un error al consultar la IA. Intenta de nuevo.",
            });
        } finally {
            setLoading(false);
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            if (!loading && input.trim()) {
                // Reutilizamos la lógica de submit para mantener validaciones consistentes
                handleSubmit(event);
            }
        }
    };

    return (
        <div className="dropdown dropdown-top dropdown-end fixed bottom-12 right-12 z-[999]">
            <button
                type="button"
                className="btn btn-primary btn-circle shadow-lg mt-5"
                tabIndex={0}
                aria-label="Abrir asistente IA"
                onClick={() => setOpen((prev) => !prev)}
            >
                <RobotIcon size={20} />
            </button>
            <ul tabIndex="-1" className="dropdown-content p-5 flex flex-col bg-base-100/95 border border-base-200 shadow-xl backdrop-blur w-[min(92vw,380px)] h-[min(70vh,520px)] rounded-lg">
                {open ? (
                    <>
                        <div className="flex items-center justify-between">
                            <div className="flex items-center gap-2 text-base font-semibold">
                                <RobotIcon size={18} />
                                Asistente IA
                            </div>
                            <button
                                type="button"
                                className="btn btn-ghost btn-sm btn-circle"
                                onClick={() => setOpen(false)}
                            >
                                <CloseIcon size={18} />
                            </button>
                        </div>

                        <div className="flex-1 overflow-y-auto space-y-3 pr-1">
                            {messages.map((message, index) => (
                                <div
                                    key={`${message.role}-${index}`}
                                    className={`chat ${message.role === "user" ? "chat-end" : "chat-start"
                                        }`}
                                >
                                    <div
                                        className={`chat-bubble text-sm ${message.role === "user"
                                            ? "chat-bubble-primary"
                                            : "chat-bubble-secondary"
                                            }`}
                                    >
                                        {message.role === "assistant" ? (
                                            <div className="prose prose-sm max-w-none prose-headings:mb-2 prose-p:mb-1 prose-ul:mb-1 prose-ol:mb-1 prose-li:marker:text-current">
                                                <ReactMarkdown remarkPlugins={[remarkGfm]}>
                                                    {String(message.content ?? "")}
                                                </ReactMarkdown>
                                            </div>
                                        ) : (
                                            message.content
                                        )}
                                    </div>
                                </div>
                            ))}
                            {loading && (
                                <div className="text-xs text-base-content/60">
                                    Pensando...
                                </div>
                            )}
                            <div ref={endRef} />
                        </div>
                        <form onSubmit={handleSubmit} className="flex items-end gap-2">
                            <textarea
                                className="textarea textarea-bordered w-full resize-none h-20 focus:outline-0 focus:border-primary bg-transparent"
                                placeholder="Escribe tu pregunta..."
                                value={input}
                                onChange={(event) => setInput(event.target.value)}
                                onKeyDown={handleKeyDown}
                                disabled={loading}
                            />
                            <button
                                type="submit"
                                className="btn btn-primary"
                                disabled={loading || !input.trim()}
                            >
                                Enviar
                            </button>
                        </form>
                    </>
                ) : null}
            </ul>
        </div>
    );
}
