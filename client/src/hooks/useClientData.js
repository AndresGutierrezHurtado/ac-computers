"use client";

import { useRouter } from "next/navigation";
import React, { useState, useEffect, useRef } from "react";
import Swal from "sweetalert2";
import { getAuthToken } from "@/hooks/useAuthSession";

const API_URL = process.env.NEXT_PUBLIC_API_URL;

export const FetchData = async (endpoint, options) => {
    const token = getAuthToken();
    const isFormData = options?.body instanceof FormData;
    const headers = {
        accept: "application/json",
        ...(options?.headers || {}),
    };

    if (!isFormData) {
        headers["content-type"] = "application/json";
    }

    if (token && !headers.Authorization) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${API_URL}${endpoint}`, {
        method: "GET",
        ...options,
        headers,
    });

    const data = await response.json();
    const authToken = response.headers.get("authorization");

    if (authToken) return { ...data, authToken };

    return data;
};

export function createStreamer(endpoint) {
    let controller = null;

    async function stream(method, endpoint, body, handlers) {
        if (controller) controller.abort();

        controller = new AbortController();

        try {
            const token = getAuthToken();
            const response = await fetch(`${API_URL}${endpoint}`, {
                method,
                body: JSON.stringify(body),
                headers: {
                    "Content-Type": "application/json",
                    accept: "text/event-stream",
                    ...(token ? { Authorization: `Bearer ${token}` } : {}),
                },
                signal: controller.signal,
            });

            if (!response.ok || !response.body) {
                const errorMessage = await response.json().then(data => data.message);
                throw new Error(errorMessage || "Error al conectar con el servidor");
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let buffer = "";

            const emit = (data) => {
                if (!data) return;
                let payload = data;
                try {
                    payload = JSON.parse(data);
                } catch (error) {
                    // Keep raw data when parsing fails.
                }
                handlers.onChunk?.(payload);
            };

            while (true) {
                const { done, value } = await reader.read();

                if (done) break;

                buffer += decoder.decode(value, { stream: true });
                const lines = buffer.split(/\r?\n/);
                buffer = lines.pop() ?? "";
                for (const line of lines) {
                    if (!line.startsWith("data:")) continue;
                    const data = line.slice(5).trimStart();
                    if (!data || data === "[DONE]") continue;
                    emit(data);
                }
            }

            handlers.onComplete?.();
        } catch (error) {
            handlers.onError?.(error);
        }
    }

    function cancel() {
        if (!controller) return;
        controller.abort();
        controller = null;
    }

    return {
        stream,
        cancel,
    };
}

export const useGetData = (endpoint) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [trigger, setTrigger] = useState(0);
    const router = useRouter();

    useEffect(() => {
        if (!endpoint) {
            setLoading(false);
            setData(null);
            return;
        }
        setLoading(true);
        const getData = async () => {
            const response = await FetchData(endpoint);
            setLoading(false);
            setData(response.data);
        };

        getData();
    }, [endpoint, trigger, router.asPath]);

    const reload = () => setTrigger((prev) => prev + 1);

    return { data, loading, reload };
};

export const useStreamData = (endpoint) => {
    const [text, setText] = useState("");
    const [status, setStatus] = useState("idle");
    const hasTextRef = useRef(false);

    useEffect(() => {
        if (!endpoint) return;
        setText("");
        setStatus("loading");
        hasTextRef.current = false;

        const eventSource = new EventSource(`${API_URL}${endpoint}`);

        eventSource.onopen = () => {
            setStatus("streaming");
        };
        eventSource.onmessage = (event) => {
            if (!event?.data) return;
            try {
                const parsed = JSON.parse(event.data);
                const chunk = parsed?.data?.overview;
                if (typeof chunk === "string" && chunk.length > 0) {
                    hasTextRef.current = true;
                    setText((prev) => prev + chunk);
                    return;
                }
            } catch (error) {
                // Fallback: append raw data if it's not JSON.
            }
            hasTextRef.current = true;
            setText((prev) => prev + event.data);
        };

        eventSource.onerror = () => {
            eventSource.close();
            setStatus((current) => {
                if (current === "streaming" && hasTextRef.current) {
                    return "streaming";
                }
                return "error";
            });
            setText((current) => {
                if (current?.trim()) return current;
                return "Hubo un error al generar el resumen del producto.";
            });
        };

        return () => {
            eventSource.close();
        };
    }, [endpoint]);

    return { text, status };
};

export const usePaginateData = (endpoint) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [trigger, setTrigger] = useState(0);
    const router = useRouter();

    useEffect(() => {
        if (!endpoint) return;

        const getData = async () => {
            const response = await FetchData(endpoint);
            setLoading(false);
            setData({
                data: response.data || [],
                total: response.total ?? 0,
            });
        };

        getData();
    }, [endpoint, trigger, router.asPath]);

    const reload = () => setTrigger((prev) => prev + 1);

    return {
        ...(data || {}), // data, total
        loading,
        reload,
    };
};

export const usePostData = async (endpoint, body = {}) => {
    const response = await FetchData(endpoint, {
        method: "POST",
        body: JSON.stringify(body),
    });

    if (response.success) {
        Swal.fire({
            icon: "success",
            title: "Acción realizada correctamente",
            text: response.message,
            timer: 8000,
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: response.message,
            timer: 8000,
        });
    }

    return response;
};

export const usePostFormData = async (endpoint, formData) => {
    const response = await FetchData(endpoint, {
        method: "POST",
        body: formData,
    });

    if (response.success) {
        Swal.fire({
            icon: "success",
            title: "Acción realizada correctamente",
            text: response.message,
            timer: 8000,
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: response.message,
            timer: 8000,
        });
    }

    return response;
};

export const usePutData = async (endpoint, body = {}) => {
    const response = await FetchData(endpoint, {
        method: "PUT",
        body: JSON.stringify(body),
    });

    if (response.success) {
        Swal.fire({
            icon: "success",
            title: "Acción realizada correctamente",
            text: response.message,
            timer: 8000,
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: response.message,
            timer: 8000,
        });
    }

    return response;
};

export const usePutFormData = async (endpoint, formData) => {
    const response = await FetchData(endpoint, {
        method: "PUT",
        body: formData,
    });

    if (response.success) {
        Swal.fire({
            icon: "success",
            title: "Acción realizada correctamente",
            text: response.message,
            timer: 8000,
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: response.message,
            timer: 8000,
        });
    }

    return response;
};

export const useDeleteData = async (endpoint, body = {}) => {
    const response = await FetchData(endpoint, {
        method: "DELETE",
        body: JSON.stringify(body),
    });

    if (response.success) {
        Swal.fire({
            icon: "success",
            title: "Acción realizada correctamente",
            text: response.message,
            timer: 8000,
        });
    } else {
        Swal.fire({
            icon: "error",
            title: "Error",
            text: response.message,
            timer: 8000,
        });
    }

    return response;
};
