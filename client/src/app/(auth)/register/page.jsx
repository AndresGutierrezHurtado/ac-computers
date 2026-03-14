"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

import TextField from "@/molecules/TextField";
import AuthCard from "@/organisms/AuthCard";
import AuthPromo from "@/organisms/AuthPromo";
import AuthSplitTemplate from "@/templates/AuthSplitTemplate";

import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

export default function Register() {
    const router = useRouter();
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (submitting) return;
        setSubmitting(true);

        const formData = new FormData(event.target);
        const data = Object.fromEntries(formData);
        const validation = useValidateform(data, "register-form");

        if (!validation.success) {
            setSubmitting(false);
            return;
        }

        const response = await usePostData("/auth/register", {
            firstName: data.user_name,
            lastName: data.user_lastname,
            email: data.user_email,
            password: data.user_password,
            roleId: 3,
        });

        if (response?.success) {
            router.push("/login");
        }

        setSubmitting(false);
    };

    return (
        <AuthSplitTemplate
            left={
                <AuthPromo
                    title="¿Ya tienes una cuenta?"
                    description="Si ya tienes una cuenta puedes iniciar sesión en el siguiente botón"
                    linkHref="/login"
                    linkLabel="Iniciar sesión"
                    align="right"
                />
            }
            right={
                <AuthCard title="AC COMPUTERS" subtitle="Regístrate">
                    <form onSubmit={handleSubmit}>
                        <div className="space-y-4">
                            <TextField
                                label="Nombre:"
                                name="user_name"
                                placeholder="Ingresa tu nombre"
                                autoComplete="given-name"
                                required
                                className="input-bordered w-full"
                            />
                            <TextField
                                label="Apellidos:"
                                name="user_lastname"
                                placeholder="Ingresa tus apellidos"
                                autoComplete="family-name"
                                required
                                className="input-bordered w-full"
                            />
                            <TextField
                                label="Correo electrónico:"
                                name="user_email"
                                type="email"
                                placeholder="Ingresa tu correo electrónico"
                                autoComplete="email"
                                required
                                className="input-bordered w-full"
                            />
                            <TextField
                                label="Contraseña:"
                                name="user_password"
                                type="password"
                                placeholder="Ingresa tu contraseña"
                                autoComplete="new-password"
                                required
                                className="input-bordered w-full"
                            />
                            <button className="btn btn-primary font-medium mt-4" disabled={submitting}>
                                {submitting ? "Creando cuenta..." : "Crear cuenta"}
                            </button>
                        </div>
                    </form>
                </AuthCard>
            }
        />
    );
}
