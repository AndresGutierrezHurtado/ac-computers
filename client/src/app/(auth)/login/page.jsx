"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useState } from "react";
import { GoogleLogin } from "@react-oauth/google";
import { toast } from "react-toastify";
import Swal from "sweetalert2";

// ATOMIC DESIGN COMPONENTS
import { LoginIcon } from "@/atoms/Icons";
import Divider from "@/atoms/Divider";
import Button from "@/atoms/Button";
import TextField from "@/molecules/TextField";
import AuthCard from "@/organisms/AuthCard";
import AuthPromo from "@/organisms/AuthPromo";
import AuthSplitTemplate from "@/templates/AuthSplitTemplate";

// HOOKS AND FUNCTIONS
import { setAuthSession } from "@/hooks/useAuthSession";
import { usePageTitle } from "@/hooks/usePageTitle";
import { usePostData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

export default function Login() {
    const router = useRouter();
    const [submitting, setSubmitting] = useState(false);

    usePageTitle("Iniciar sesión");

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
                        <div className="space-y-2">
                            <TextField
                                label="Correo electrónico:"
                                name="user_email"
                                placeholder="Ingresa tu correo electrónico"
                                type="email"
                                autoComplete="email"
                                required
                            />
                            <TextField
                                label="Contraseña:"
                                name="user_password"
                                placeholder="Ingresa tu contraseña"
                                type="password"
                                autoComplete="current-password"
                                required
                                togglePassword
                            />
                            <div>
                                <Link
                                    href="/forgot"
                                    className="link link-hover text-primary font-medium text-base"
                                >
                                    Olvidaste tu contraseña?
                                </Link>
                            </div>
                            <div className="space-y-4">
                                <Button
                                    type="submit"
                                    className="btn-primary font-medium mt-4 w-full"
                                    disabled={submitting}
                                    leftIcon={<LoginIcon size={16} />}
                                >
                                    {submitting ? "Ingresando..." : "Iniciar Sesión"}
                                </Button>
                                <Divider text="O inicia con" />
                                <GoogleLogin
                                    onSuccess={handleGoogleLogin}
                                    onError={(error) => toast.error(error.message)}
                                    useOneTap
                                />
                            </div>
                        </div>
                    </form>
                </AuthCard>
            }
        />
    );
}
