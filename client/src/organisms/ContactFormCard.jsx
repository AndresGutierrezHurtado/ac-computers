"use client";

import { useState } from "react";
import { UploadIcon } from "@/atoms/Icons";
import TextField from "@/molecules/TextField";
import TextareaField from "@/molecules/TextareaField";
import { usePostData } from "@/hooks/useClientData";

export default function ContactFormCard() {
    const [form, setForm] = useState({
        subject: "",
        name: "",
        email: "",
        message: "",
    });
    const [submitting, setSubmitting] = useState(false);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (submitting) return;
        setSubmitting(true);

        const response = await usePostData("/contact", {
            subject: form.subject,
            name: form.name,
            email: form.email,
            message: form.message,
        });

        if (response?.success) {
            setForm({ subject: "", name: "", email: "", message: "" });
        }

        setSubmitting(false);
    };

    const contactInputClass = "bg-transparent focus-within:border-primary";

    return (
        <div className="card bg-black/20 h-fit w-full">
            <div className="card-body [&_p]:grow-0 px-8 py-10">
                <form onSubmit={handleSubmit} className="space-y-2">
                    <div className="flex flex-col gap-2">
                        <h2 className="text-3xl font-extrabold">¡Queremos escucharte!</h2>
                        <p className="leading-[1.35]">
                            Dejanos tu mensaje y nos pondremos en contacto.
                        </p>
                    </div>

                    <TextField
                        label="Nombre:"
                        name="name"
                        placeholder="Ingresa tu nombre"
                        value={form.name}
                        onChange={handleChange}
                        required
                        className={contactInputClass}
                    />

                    <TextField
                        label="Asunto:"
                        name="subject"
                        placeholder="Ingresa el asunto de tu mensaje"
                        value={form.subject}
                        onChange={handleChange}
                        required
                        className={contactInputClass}
                    />

                    <TextField
                        label="Correo electrónico:"
                        name="email"
                        type="email"
                        placeholder="Ingresa tu correo electrónico"
                        value={form.email}
                        onChange={handleChange}
                        required
                        className={contactInputClass}
                    />

                    <TextareaField
                        label="Mensaje:"
                        name="message"
                        placeholder="Ingresa tu mensaje"
                        value={form.message}
                        onChange={handleChange}
                        required
                        resizable={false}
                        rows={6}
                        className="w-full bg-transparent focus:border-primary resize-none"
                    />

                    <div className="form-control flex flex-col gap-1 w-full pt-5">
                        <button
                            className="btn w-full btn-primary"
                            type="submit"
                            disabled={submitting}
                        >
                            <UploadIcon size={20} />
                            {submitting ? "Enviando..." : "Enviar mensaje"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
