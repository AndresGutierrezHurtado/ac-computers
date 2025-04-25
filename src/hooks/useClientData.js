import { useRouter } from "next/navigation";
import React, { useState, useEffect } from "react";
import Swal from "sweetalert2";

const API_URL = process.env.NEXT_PUBLIC_APP_DOMAIN;

export const FetchData = async (endpoint, options) => {
    const request = await fetch(`${API_URL}/api${endpoint}`, {
        headers: {
            "content-type": "application/json",
            accept: "application/json",
        },
        method: "GET",
        ...options,
    });

    return request.json();
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
        const getData = async () => {
            const response = await FetchData(endpoint);
            setLoading(false);
            setData({
                data: response.data,
                page: response.page,
                limit: response.limit,
                count: response.count,
            });
        };

        getData();
    }, [endpoint, trigger, router.asPath]);

    const reload = () => setTrigger((prev) => prev + 1);

    return {
        ...(data || {}), // data, page, limit, count
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
