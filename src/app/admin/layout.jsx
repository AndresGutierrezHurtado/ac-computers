"use client";

import { useSession } from "next-auth/react";
import { useEffect } from "react";

export default function AdminLayout({ children }) {
    const { data: session, status } = useSession();
    const user = session?.user;

    useEffect(() => {
        if (user.role_id === 2) return;
        if (status === "loading") return;
        if (status === "unauthenticated") {
            router.push("/");
        }
    }, [user, status]);

    return <>{children}</>;
}
