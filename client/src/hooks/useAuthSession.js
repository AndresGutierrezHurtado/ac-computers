"use client";

import { useSyncExternalStore } from "react";

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
    notify();
};

export const clearAuthSession = () => {
    authState.token = null;
    authState.user = null;
    notify();
};

export const getAuthToken = () => authState.token;

export const getAuthUser = () => authState.user;

export const useAuthSession = () => {
    const state = useSyncExternalStore(subscribe, getCachedSnapshot, getCachedSnapshot);

    return {
        token: state.token,
        user: state.user,
        isAuthenticated: Boolean(state.token),
    };
};
