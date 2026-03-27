"use client";

import { useState } from "react";

import { EnvelopeIcon } from "@/atoms/Icons";
import Button from "@/atoms/Button";
import TextField from "@/molecules/TextField";

import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";
import { usePageTitle } from "@/hooks/usePageTitle";

export default function ForgotPage() {
    const [submitting, setSubmitting] = useState(false);

    usePageTitle("Recuperar contraseña");

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (submitting) return;

        const formData = Object.fromEntries(new FormData(event.target));
        const validation = useValidateform(formData, "forgot-form");

        if (!validation.success) return;

        setSubmitting(true);
        await usePostData("/auth/forgot-password", {
            email: validation.data.user_email,
        });
        setSubmitting(false);
    };

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
                        <form className="w-full space-y-4" onSubmit={handleSubmit}>
                            <TextField
                                label="Correo electrónico:"
                                name="user_email"
                                type="email"
                                placeholder="Ingresa tu correo electrónico"
                                required
                                className="input-bordered w-full"
                            />
                            <div className="w-full">
                                <Button
                                    type="submit"
                                    className="btn btn-primary btn-wide font-medium"
                                    disabled={submitting}
                                    loading={submitting}
                                    leftIcon={<EnvelopeIcon size={16} />}
                                >
                                    Recuperar cuenta
                                </Button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </>
    );
}
