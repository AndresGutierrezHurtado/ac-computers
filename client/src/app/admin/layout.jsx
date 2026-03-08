"use client";

import AdminRouteGuard from "@/organisms/AdminRouteGuard";

export default function AdminLayout({ children }) {
    return <AdminRouteGuard>{children}</AdminRouteGuard>;
}
