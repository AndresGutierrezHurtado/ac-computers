"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import { FetchData } from "@/hooks/useClientData";
import { clearAuthSession, getAuthToken, setAuthSession } from "@/hooks/useAuthSession";

const ALLOWED_ROLES = new Set(["SUPERUSER", "ADMINISTRATOR"]);

const normalizeRole = (roleName) => (roleName || "").trim().toUpperCase();

export default function AdminRouteGuard({ children }) {
    const router = useRouter();
    const [checking, setChecking] = useState(true);

    useEffect(() => {
        let active = true;

        const verifySession = async () => {
            const token = getAuthToken();
            if (!token) {
                clearAuthSession();
                router.replace("/");
                return;
            }

            const response = await FetchData("/auth/session");
            if (!response?.success || !response?.data) {
                clearAuthSession();
                router.replace("/");
                return;
            }

            const roleName = normalizeRole(response.data.role?.name);
            if (!ALLOWED_ROLES.has(roleName)) {
                clearAuthSession();
                router.replace("/");
                return;
            }

            setAuthSession({ token, user: response.data });
            if (active) {
                setChecking(false);
            }
        };

        verifySession();

        return () => {
            active = false;
        };
    }, [router]);

    if (checking) {
        return (
            <div className="min-h-[60vh] flex items-center justify-center">
                <p className="text-lg text-base-content/70">Verificando sesión...</p>
            </div>
        );
    }

    return <>{children}</>;
}
