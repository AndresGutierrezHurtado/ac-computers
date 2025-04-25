"use client";

import React, { useEffect } from "react";
import { useParams, useRouter } from "next/navigation";

// Hooks
import { useGetData, usePutData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";
import Loading from "@/components/loading";

export default function Page() {
    const { id } = useParams();
    const router = useRouter();

    const { data: recovery, loading } = useGetData(`/auth/forgot/${id}`);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));
        const validation = useValidateform(data, "reset-form");

        if (!validation.success) return;

        const response = await usePutData(`/auth/forgot/${id}`, data);
        if (!response.success) return;

        e.target.reset();
        router.push("/login");
    };

    useEffect(() => {
        document.title = "Cambiar contraseña | AC Computers";
    }, []);

    if (loading) return <Loading />;
    if (!recovery) {
        return (
            <p className="text-center py-10 mt-[100px] text-2xl font-bold">
                No se encontró la recuperación, debe estar expirada o ya debio ser usada
            </p>
        );
    }
    return (
        <>
            <section className="w-full px-4 mt-[100px]">
                <div className="w-full max-w-xl mx-auto py-10 space-y-10">
                    <h2 className="text-4xl font-extrabold uppercase text-center">
                        AC <span className="text-primary italic">Computers</span>
                    </h2>
                    <div className="w-full p-5 rounded-lg bg-base-200 border border-base-300 shadow-lg shadow-base-300/30 space-y-5">
                        <div className="space-y-2">
                            <h2 className="text-3xl font-bold">Cambiar contraseña</h2>
                            <p className="text-base-content/80">
                                Ingresa tu nueva contraseña para cambiarla.
                            </p>
                        </div>
                        <form className="w-full" onSubmit={handleSubmit}>
                            <fieldset className="w-full fieldset">
                                <label className="fieldset-label text-sm after:content-['*'] after:text-red-500">
                                    Nueva contraseña
                                </label>
                                <input
                                    name="user_password"
                                    className="w-full input focus:outline-0 focus:border-primary"
                                    placeholder="Ingresa tu nueva contraseña"
                                    type="password"
                                />
                            </fieldset>
                            <fieldset className="w-full fieldset">
                                <button
                                    type="submit"
                                    className="btn btn-primary btn-wide font-medium"
                                >
                                    Cambiar contraseña
                                </button>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </section>
        </>
    );
}
