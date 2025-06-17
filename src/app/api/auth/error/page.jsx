import Link from "next/link";
import React from "react";

export const metadata = {
    title: "Error de autenticación | AC Computers",
    description: "Hubo un error al autenticarte. Por favor, vuelve a iniciar sesión.",
};

export default async function Page({ searchParams }) {
    const { error } = await searchParams;

    return (
        <div className="hero bg-base-200 min-h-screen">
            <div className="hero-content text-center">
                <div className="max-w-md">
                    <h1 className="text-5xl font-bold">Hubo un error</h1>
                    <div className="text-lg py-6">
                        <p>{error}</p>
                        <p>Por favor, vuelve a iniciar sesión.</p>
                    </div>

                    <Link href="/login">
                        <button className="btn btn-primary">Iniciar sesión</button>
                    </Link>
                </div>
            </div>
        </div>
    );
}
