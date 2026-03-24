"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useState } from "react";
import Swal from "sweetalert2";
import { GoogleLogin } from "@react-oauth/google";
import { toast } from "react-toastify";

import TextField from "@/molecules/TextField";
import AuthCard from "@/organisms/AuthCard";
import AuthPromo from "@/organisms/AuthPromo";
import AuthSplitTemplate from "@/templates/AuthSplitTemplate";

import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";
import { setAuthSession } from "@/hooks/useAuthSession";

export default function Login() {
    const router = useRouter();
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (submitting) return;
        setSubmitting(true);

        const formData = new FormData(event.target);
        const data = Object.fromEntries(formData);
        const validation = useValidateform(data, "login-form");

        if (!validation.success) {
            setSubmitting(false);
            return;
        }

        const response = await usePostData("/auth/login", {
            email: data.user_email,
            password: data.user_password,
        });

        if (!response?.success) return;

        // get the token from the response headers
        const rawToken = response.authToken || "";
        const token = rawToken.startsWith("Bearer ") ? rawToken.slice(7) : rawToken;

        if (!token) {
            return Swal.fire({
                icon: "error",
                title: "Error",
                text: "No se recibió el token de autenticación",
                timer: 8000,
            });
        }

        setAuthSession({ token, user: response.data });
        router.push("/profile");

        setSubmitting(false);
    };

    const handleGoogleLogin = useCallback(async ({ credential }) => {
        const response = await usePostData("/auth/google", {
            credential,
        });

        if (!response?.success) return;

        // get the token from the response headers
        const rawToken = response.authToken || "";
        const token = rawToken.startsWith("Bearer ") ? rawToken.slice(7) : rawToken;

        if (!token) {
            return Swal.fire({
                icon: "error",
                title: "Error",
                text: "Error al iniciar sesión con Google",
                timer: 8000,
            });
        }

        setAuthSession({ token, user: response.data });
        router.push("/");
    }, []);

    return (
        <AuthSplitTemplate
            reverse
            left={
                <AuthPromo
                    title="¿No has creado una cuenta?"
                    description="Si aún no tienes una cuenta, puedes crearla en el siguiente botón"
                    linkHref="/register"
                    linkLabel="Registrarse"
                />
            }
            right={
                <AuthCard title="AC COMPUTERS" subtitle="Iniciar Sesión" brandHref="/">
                    <form onSubmit={handleSubmit}>
                        <div className="space-y-4">
                            <TextField
                                label="Correo electrónico:"
                                name="user_email"
                                placeholder="Ingresa tu correo electrónico"
                                type="email"
                                autoComplete="email"
                                required
                                className="input-bordered w-full"
                            />
                            <TextField
                                label="Contraseña:"
                                name="user_password"
                                placeholder="Ingresa tu contraseña"
                                type="password"
                                autoComplete="current-password"
                                required
                                className="input-bordered w-full"
                            />
                            <div>
                                <Link
                                    href="/forgot"
                                    className="link link-hover text-primary font-medium text-base"
                                >
                                    Olvidaste tu contraseña?
                                </Link>
                            </div>
                            <button
                                className="btn btn-primary font-medium mt-4"
                                disabled={submitting}
                            >
                                {submitting ? "Ingresando..." : "Iniciar Sesión"}
                            </button>
                            <div className="divider">O inicia con</div>
                            <GoogleLogin
                                onSuccess={handleGoogleLogin}
                                onError={(error) => toast.error(error.message)}
                                useOneTap
                            />
                        </div>
                    </form>
                </AuthCard>
            }
        />
    );
}
