import { NextResponse } from "next/server";
import { getToken } from "next-auth/jwt";

export async function middleware(req) {
    const token = await getToken({ req, secret: process.env.SESSION_SECRET });
    const adminPages = ["/admin", "/admin/users", "/admin/products"];
    const notAuthNeeded = ["/login", "/register"];

    // not auth needed pages
    if (notAuthNeeded.some((path) => req.nextUrl.pathname.startsWith(path)) && token) {
        console.log("Acceso denegado para usuario autenticado.");
        return NextResponse.redirect(new URL("/", req.url));
    }

    // admin pages
    if (
        adminPages.some((path) => req.nextUrl.pathname.startsWith(path)) &&
        (!token || token?.role !== 2)
    ) {
        console.log("Acceso denegado para usuario sin permisos.");
        return NextResponse.redirect(new URL("/", req.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: "/:path*",
};
