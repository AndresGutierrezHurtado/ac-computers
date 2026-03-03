"use client";

import { useRouter } from "next/navigation";
import React, { useState, useEffect } from "react";
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

    console.log(authToken, Object.fromEntries(response.headers.entries()));

    if (authToken) {
        return { ...data, authToken };
    }

    return data;
};

export const useGetData = (endpoint) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [trigger, setTrigger] = useState(0);
    const router = useRouter();

    useEffect(() => {
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
