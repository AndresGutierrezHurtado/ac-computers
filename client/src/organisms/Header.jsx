"use client";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useAuthSession, clearAuthSession } from "@/hooks/useAuthSession";
import { usePostData } from "@/hooks/useClientData";

// Icons
import {
    CloseIcon,
    ComputerIcon,
    GearIcon,
    HomeIcon,
    PhoneIcon,
    TrashIcon,
    UserIcon,
} from "@/atoms/Icons";
import { useEffect, useRef } from "react";

export default function Header() {
    const router = useRouter();
    const headerRef = useRef(null);
    const { isAuthenticated, user } = useAuthSession();
    const roleName = user?.role?.name?.toLowerCase();
    const isAdmin = roleName === "superuser" || roleName === "administrator";
    const accountLabel = isAuthenticated
        ? user?.firstName || "Mi cuenta"
        : "Cuenta";

    useEffect(() => {
        const classes = ["bg-black/20", "px-5"];
        const handleScroll = () => {
            if (window.scrollY > 0) headerRef.current.classList.add(...classes);
            else headerRef.current.classList.remove(...classes);
        };

        window.addEventListener("scroll", handleScroll);

        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, []);

    const handleLogout = async () => {
        try {
            await usePostData("/auth/logout");
        } finally {
            clearAuthSession();
            router.push("/");
        }
    };

    return (
        <div className="fixed w-full top-0 z-50">
            <input id="responsive-drawer" type="checkbox" className="drawer-toggle" />
            <div className="drawer-content flex flex-col">
                {/* Navbar */}
                <div className="w-full px-3 py-2">
                    <div
                        id="header"
                        ref={headerRef}
                        className="navbar max-w-[1200px] mx-auto w-full rounded-full duration-300 py-0 backdrop-blur-sm"
                    >
                        {/* Responsive button */}
                        <div className="flex-none lg:hidden">
                            <label
                                htmlFor="responsive-drawer"
                                aria-label="open sidebar"
                                className="btn btn-square btn-ghost"
                            >
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    className="inline-block h-6 w-6 stroke-current"
                                >
                                    <path
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        strokeWidth="2"
                                        d="M4 6h16M4 12h16M4 18h16"
                                    ></path>
                                </svg>
                            </label>
                        </div>

                        <Link href="/" className="navbar-start">
                            <div
                                className="tooltip tooltip-neutral tooltip-bottom"
                                data-tip="Ir al inicio"
                            >
                                <h2 className="text-nowrap text-start text-lg sm:text-2xl md:text-[27px] text-primary uppercase font-extrabold tracking-tight">
                                    AC COMPUTERS
                                </h2>
                            </div>
                        </Link>
                        <div className="hidden flex-none lg:block navbar-center">
                            <ul className="menu menu-horizontal text-lg [&>li:hover]:text-primary [&>li:hover]:scale-105 [&>li:hover]:duration-300 [&>li>a:hover]:bg-transparent [&>li>a:focus]:bg-transparent [&>li>a:focus]:text-primary">
                                {/* Navbar menu content here */}
                                <li>
                                    <Link
                                        href="/products"
                                        className={`${router.pathname === "/products" &&
                                            "text-primary font-semibold"
                                            }`}
                                    >
                                        Productos
                                    </Link>
                                </li>
                                <li>
                                    <Link
                                        href="/contact"
                                        className={`${router.pathname === "/contact" &&
                                            "text-primary font-semibold"
                                            }`}
                                    >
                                        Contáctanos
                                    </Link>
                                </li>
                            </ul>
                        </div>
                        <div className="navbar-end">
                            <div className="dropdown dropdown-end">
                                <div tabIndex="0" role="button" className="btn btn-ghost avatar">
                                    {accountLabel}
                                </div>
                                <ul
                                    tabIndex="0"
                                    className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
                                >
                                    {isAuthenticated ? (
                                        <>
                                            <li>
                                                <Link href="/profile">
                                                    <UserIcon />
                                                    Mi cuenta
                                                </Link>
                                            </li>
                                            {isAdmin && (
                                                <>
                                                    <li>
                                                        <Link
                                                            href="/admin/users"
                                                            className="text-blue-400"
                                                        >
                                                            <GearIcon />
                                                            Usuarios
                                                        </Link>
                                                    </li>
                                                    <li>
                                                        <Link
                                                            href="/admin/products"
                                                            className="text-blue-400"
                                                        >
                                                            <GearIcon />
                                                            Productos
                                                        </Link>
                                                    </li>
                                                </>
                                            )}
                                            <li>
                                                <button
                                                    type="button"
                                                    onClick={handleLogout}
                                                    className="text-red-400 flex items-center gap-2 w-full"
                                                >
                                                    <TrashIcon />
                                                    Cerrar sesión
                                                </button>
                                            </li>
                                        </>
                                    ) : (
                                        <>
                                            <li>
                                                <Link href="/login">Iniciar sesión</Link>
                                            </li>
                                            <li>
                                                <Link href="/register">Registrarme</Link>
                                            </li>
                                        </>
                                    )}
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div className="drawer-side">
                <ul className="menu bg-base-200 min-h-full w-80 p-4 justify-between">
                    <div>
                        <li>
                            <Link
                                href="/"
                                className={`${router.pathname === "/" && "text-primary font-semibold"
                                    }`}
                            >
                                <HomeIcon />
                                <p>Inicio</p>
                            </Link>
                        </li>
                        <li>
                            <Link
                                href="/products"
                                className={`${router.pathname === "/products" && "text-primary font-semibold"
                                    }`}
                            >
                                <ComputerIcon />
                                <p>Productos</p>
                            </Link>
                        </li>
                        <li>
                            <Link
                                href="/contact"
                                className={`${router.pathname === "/contact" && "text-primary font-semibold"
                                    }`}
                            >
                                <PhoneIcon />
                                <p>Contáctanos</p>
                            </Link>
                        </li>
                    </div>
                    <div>
                        <label
                            htmlFor="responsive-drawer"
                            aria-label="close sidebar"
                            className="drawer-overlay"
                        >
                            <li>
                                <a>
                                    <CloseIcon />
                                    <p>Cerrar</p>
                                </a>
                            </li>
                        </label>
                    </div>
                </ul>
            </div>
        </div>
    );
}
