"use client";

import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import Swal from "sweetalert2";

import AuthCard from "@/organisms/AuthCard";
import TextField from "@/molecules/TextField";

import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

export default function SetPasswordPage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const token = searchParams.get("token") || "";

    const [form, setForm] = useState({ password: "", confirmPassword: "" });
    const [submitting, setSubmitting] = useState(false);

    const handleChange = (field) => (event) => {
        setForm((prev) => ({ ...prev, [field]: event.target.value }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (submitting) return;

        const validation = useValidateform(
            {
                password: form.password,
                confirmPassword: form.confirmPassword,
            },
            "set-password-form",
        );

        if (!validation.success) {
            return;
        }

        if (form.password !== form.confirmPassword) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Las contraseñas no coinciden",
                timer: 8000,
            });
            return;
        }

        if (!token) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Token inválido o faltante",
                timer: 8000,
            });
            return;
        }

        setSubmitting(true);
        const response = await usePostData("/auth/set-password", {
            token,
            password: form.password,
        });

        if (response?.success) {
            router.push("/login");
        }

        setSubmitting(false);
    };

    return (
        <div className="min-h-screen flex items-center justify-center px-4 py-10 bg-base-200">
            <AuthCard title="AC COMPUTERS" subtitle="Configura tu contraseña" brandHref="/">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <TextField
                        label="Nueva contraseña:"
                        name="password"
                        placeholder="Ingresa tu contraseña"
                        type="password"
                        value={form.password}
                        onChange={handleChange("password")}
                        required
                        className="input-bordered w-full"
                    />
                    <TextField
                        label="Confirmar contraseña:"
                        name="confirmPassword"
                        placeholder="Repite tu contraseña"
                        type="password"
                        value={form.confirmPassword}
                        onChange={handleChange("confirmPassword")}
                        required
                        className="input-bordered w-full"
                    />
                    <button className="btn btn-primary w-full" disabled={submitting || !token}>
                        {submitting ? "Guardando..." : "Guardar contraseña"}
                    </button>
                </form>
                {!token ? (
                    <p className="text-sm text-red-400 mt-3">
                        El enlace no es válido o ha expirado.
                    </p>
                ) : null}
            </AuthCard>
        </div>
    );
}
