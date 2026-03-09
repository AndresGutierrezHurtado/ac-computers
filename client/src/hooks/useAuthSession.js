"use client";

import { useEffect, useSyncExternalStore } from "react";

const STORAGE_KEY = "ac-auth-session";

let hasHydrated = false;

const authState = {
    token: null,
    user: null,
};

const listeners = new Set();

const notify = () => {
    listeners.forEach((listener) => listener());
};

const subscribe = (listener) => {
    listeners.add(listener);
    return () => listeners.delete(listener);
};

const getSnapshot = () => ({
    token: authState.token,
    user: authState.user,
});

let cachedSnapshot = getSnapshot();

const getCachedSnapshot = () => {
    if (
        cachedSnapshot.token === authState.token &&
        cachedSnapshot.user === authState.user
    ) {
        return cachedSnapshot;
    }

    cachedSnapshot = {
        token: authState.token,
        user: authState.user,
    };
    return cachedSnapshot;
};

export const setAuthSession = ({ token, user }) => {
    authState.token = token || null;
    authState.user = user || null;
    if (typeof window !== "undefined") {
        const payload = JSON.stringify({ token: authState.token, user: authState.user });
        window.localStorage.setItem(STORAGE_KEY, payload);
    }
    notify();
};

export const clearAuthSession = () => {
    authState.token = null;
    authState.user = null;
    if (typeof window !== "undefined") {
        window.localStorage.removeItem(STORAGE_KEY);
    }
    notify();
};

export const getAuthToken = () => authState.token;

export const getAuthUser = () => authState.user;

export const hydrateAuthSession = () => {
    if (hasHydrated || typeof window === "undefined") return null;

    hasHydrated = true;

    try {
        const raw = window.localStorage.getItem(STORAGE_KEY);
        if (!raw) return null;
        const parsed = JSON.parse(raw);
        if (!parsed || typeof parsed !== "object") {
            window.localStorage.removeItem(STORAGE_KEY);
            return null;
        }

        authState.token = parsed.token || null;
        authState.user = parsed.user || null;
        notify();

        return { token: authState.token, user: authState.user };
    } catch (error) {
        window.localStorage.removeItem(STORAGE_KEY);
        return null;
    }
};

export const useAuthSession = () => {
    const state = useSyncExternalStore(subscribe, getCachedSnapshot, getCachedSnapshot);

    useEffect(() => {
        hydrateAuthSession();
    }, []);

    return {
        token: state.token,
        user: state.user,
        isAuthenticated: Boolean(state.token),
    };
};
