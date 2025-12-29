"use client";

import React, { useEffect } from "react";

// Hooks
import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

export default function page() {
    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));

        const validation = useValidateform(data, "forgot-form");
        if (!validation.success) return;

        const response = await usePostData("/auth/forgot", data);
        if (!response.success) return;

        e.target.reset();
    };

    useEffect(() => {
        document.title = "Recuperar contraseña | AC Computers";
    }, []);

    return (
        <>
            <section className="w-full px-4 mt-[100px]">
                <div className="w-full max-w-xl mx-auto py-10 space-y-10">
                    <h2 className="text-4xl font-extrabold uppercase text-center">
                        AC <span className="text-primary italic">Computers</span>
                    </h2>
                    <div className="w-full p-5 rounded-lg bg-base-200 border border-base-300 shadow-lg shadow-base-300/30 space-y-5">
                        <div className="space-y-2">
                            <h2 className="text-3xl font-bold">Recuperar contraseña</h2>
                            <p className="text-base-content/80">
                                Ingresa tu correo electrónico para recuperar tu cuenta.
                            </p>
                        </div>
                        <form className="w-full" onSubmit={handleSubmit}>
                            <fieldset className="w-full fieldset">
                                <label className="fieldset-label text-sm after:content-['*'] after:text-red-500">
                                    Correo electrónico:
                                </label>
                                <input
                                    name="user_email"
                                    className="w-full input focus:outline-0 focus:border-primary"
                                    placeholder="Ingresa tu correo electrónico"
                                />
                            </fieldset>
                            <fieldset className="w-full fieldset">
                                <button
                                    type="submit"
                                    className="btn btn-primary btn-wide font-medium"
                                >
                                    Recuperar contraseña
                                </button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </section>
        </>
    );
}
