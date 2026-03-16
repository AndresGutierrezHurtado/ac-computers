"use client";

import { useEffect, useRef, useState } from "react";
import ChatMessage from "@/atoms/ChatMessage";
import ChatComposer from "@/molecules/ChatComposer";
import { CloseIcon, RobotIcon } from "@/atoms/Icons";

const API_URL = process.env.NEXT_PUBLIC_API_URL;
const STORAGE_KEY_MESSAGES = "ac_ai_chat_messages_v1";

const DEFAULT_MESSAGES = [
    {
        role: "assistant",
        content: "Hola, soy el asistente de AC Computers. ¿Qué estás buscando?",
    },
];

export default function AIChatWidget() {
    const [messages, setMessages] = useState(DEFAULT_MESSAGES);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [hydrated, setHydrated] = useState(false);
    const endRef = useRef(null);

    const scrollToBottom = () => {
        endRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages, loading]);

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
        <div className="dropdown dropdown-top dropdown-end fixed bottom-12 right-12 z-[999]">
            <div
                tabIndex={0}
                role="button"
                className="btn btn-primary btn-circle shadow-lg mt-5"
                aria-label="Abrir asistente IA"
            >
                <RobotIcon size={20} />
            </div>
            <ul
                tabIndex={-1}
                className="dropdown-content m-0 flex list-none flex-col bg-base-100/95 border border-base-200 shadow-xl backdrop-blur w-[min(92vw,380px)] h-[min(70vh,520px)] rounded-lg p-0 z-[1]"
            >
                <li className="list-none flex min-h-0 flex-1 flex-col gap-3 p-5">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-2 text-base font-semibold">
                            <RobotIcon size={18} />
                            Asistente IA
                        </div>
                        <button
                            type="button"
                            className="btn btn-ghost btn-sm btn-circle"
                            onClick={closeDropdown}
                        >
                            <CloseIcon size={18} />
                        </button>
                    </div>

                    <div className="min-h-0 flex-1 space-y-3 overflow-y-auto pr-1">
                        {messages.map((message, index) => (
                            <ChatMessage
                                key={`${message.role}-${index}`}
                                role={message.role}
                                content={message.content}
                            />
                        ))}
                        {loading && (
                            <div className="text-xs text-base-content/60">Pensando...</div>
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
        </div>
    );
}
