"use client";

import { useState } from "react";
import { UploadIcon } from "@/atoms/icons";
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

                    <fieldset className="fieldset">
                        <label className="fieldset-label font-medium text-base">Nombre:</label>
                        <input
                            className="input w-full focus:outline-0 focus:border-primary bg-transparent"
                            placeholder="Ingresa tu nombre"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            required
                        />
                    </fieldset>

                    <fieldset className="fieldset">
                        <label className="fieldset-label font-medium text-base">Asunto:</label>
                        <input
                            className="input w-full focus:outline-0 focus:border-primary bg-transparent"
                            placeholder="Ingresa el asunto de tu mensaje"
                            name="subject"
                            value={form.subject}
                            onChange={handleChange}
                            required
                        />
                    </fieldset>

                    <fieldset className="fieldset">
                        <label className="fieldset-label font-medium text-base">
                            Correo electrónico:
                        </label>
                        <input
                            type="email"
                            className="input w-full focus:outline-0 focus:border-primary bg-transparent"
                            placeholder="Ingresa tu correo electrónico"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                    </fieldset>

                    <fieldset className="fieldset">
                        <label className="fieldset-label font-medium text-base">Mensaje:</label>
                        <textarea
                            className="textarea w-full focus:outline-0 focus:border-primary bg-transparent resize-none h-32"
                            placeholder="Ingresa tu mensaje"
                            name="message"
                            value={form.message}
                            onChange={handleChange}
                            required
                        ></textarea>
                    </fieldset>

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
