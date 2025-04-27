"use client";

import React, { useEffect } from "react";
import Link from "next/link";
import Swal from "sweetalert2";

// Hooks
import { useValidateform } from "@/hooks/useValidateForm";

// Components
import {
    AtIcon,
    FacebookIcon,
    InstagramIcon,
    LocationIcon,
    UploadIcon,
    WhatsappIcon,
} from "@/components/icons";

import dynamic from "next/dynamic";
const StoreMap = dynamic(() => import("@/components/map"), { ssr: false });

export default function Page() {
    const socialMedias = [
        {
            icon: <InstagramIcon size={20} />,
            url: "https://www.instagram.com/accomputersas/",
            text: "Instagram: @accomputersas",
        },
        {
            icon: <FacebookIcon size={20} />,
            url: "https://web.facebook.com/Amaliacastrode",
            text: "Facebook: Amalia Castro Ardila",
        },
        {
            icon: <WhatsappIcon size={20} />,
            url: "https://wa.me/+573118835868",
            text: "WhatsApp: 311 8835868",
        },
        {
            icon: <AtIcon size={20} />,
            url: "mailto:amaliacastro78@example.com",
            text: "Correo: amaliacastro78@gmail.com",
        },
        {
            icon: <LocationIcon size={20} />,
            url: "https://maps.app.goo.gl/qHg7MtCmABNfReT8A",
            text: "Ubicación: Cra. 15 #77 05, Bogotá",
        },
    ];

    useEffect(() => {
        document.title = "Contactanos | AC Computers";
    }, []);

    const handleFormSubmit = async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));
        const validation = useValidateform(data, "contact-form");

        if (validation.success) {
            const response = await fetch("/api/users/feedback", {
                headers: {
                    "content-type": "application/json",
                    accept: "application/json",
                },
                method: "POST",
                body: JSON.stringify(data),
            });

            const json = await response.json();

            Swal.fire({
                icon: json.success ? "success" : "error",
                title: json.message,
            });

            if (json.success) {
                e.target.reset();
            }
        }
    };

    return (
        <>
            <section className="w-full px-3">
                <div className="w-full max-w-[1200px] mx-auto py-10 mt-[100px]">
                    <div className="flex flex-col md:flex-row gap-10 w-full">
                        <div className="w-full md:w-1/2 space-y-8">
                            <div className="space-y-3">
                                <h2 className="text-4xl font-extrabold tracking-tight">
                                    Contáctanos:
                                </h2>
                                <p>
                                    Lorem ipsum dolor sit, amet consectetur adipisicing elit.
                                    Suscipit corrupti qui adipisci eius dignissimos sunt quo libero,
                                    quae voluptates magnam temporibus in reprehenderit voluptatum
                                    porro animi dolor. Minima, repudiandae at reiciendis obcaecati,
                                    necessitatibus ducimus odio fugit iure quaerat natus amet modi!
                                    Ipsam, tempore! Odit ducimus molestias dolor neque doloribus
                                    qui.
                                </p>
                            </div>
                            <ol className="space-y-2.5">
                                {socialMedias.map((socialMedia) => (
                                    <Link
                                        key={socialMedia.text}
                                        href={socialMedia.url}
                                        target="_blank"
                                        role="alert"
                                        className="flex items-center gap-5 bg-black/10 hover:bg-black/20 duration-300 rounded-md p-5"
                                    >
                                        {socialMedia.icon}
                                        <div>
                                            <div className="text-lg">{socialMedia.text}</div>
                                        </div>
                                    </Link>
                                ))}
                            </ol>
                        </div>
                        <div className="card bg-black/20 h-fit w-full md:w-1/2">
                            <div className="card-body [&_p]:grow-0 px-8 py-10">
                                <form onSubmit={handleFormSubmit} className="space-y-2">
                                    <div className="flex flex-col gap-2">
                                        <h2 className="text-3xl font-extrabold">
                                            ¡Queremos escucharte!
                                        </h2>
                                        <p className="leading-[1.35]">
                                            Dejanos tu mensaje y nos pondremos en contacto.
                                        </p>
                                    </div>

                                    <fieldset className="fieldset">
                                        <label className="fieldset-label font-medium text-base">
                                            Correo electrónico:
                                        </label>
                                        <input
                                            className="input w-full focus:outline-0 focus:border-primary bg-transparent"
                                            placeholder="Ingresa tu correo electrónico"
                                            name="user_email"
                                        />
                                    </fieldset>

                                    <fieldset className="fieldset">
                                        <label className="fieldset-label font-medium text-base">
                                            Asunto:
                                        </label>
                                        <input
                                            className="input w-full focus:outline-0 focus:border-primary bg-transparent"
                                            placeholder="Ingresa el asunto de tu mensaje"
                                            name="email_subject"
                                        />
                                    </fieldset>

                                    <fieldset className="fieldset">
                                        <label className="fieldset-label font-medium text-base">
                                            Mensaje:
                                        </label>
                                        <textarea
                                            className="textarea w-full focus:outline-0 focus:border-primary bg-transparent resize-none h-32"
                                            placeholder="Ingresa tu mensaje"
                                            name="email_message"
                                        ></textarea>
                                    </fieldset>

                                    <div className="form-control flex flex-col gap-1 w-full pt-5">
                                        <button className="btn w-full btn-primary">
                                            <UploadIcon size={20} />
                                            Enviar mensaje
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <section className="w-full px-3">
                <div className="w-full max-w-[1200px] mx-auto py-10">
                    <div className="space-y-5">
                        <h2 className="text-4xl font-extrabold tracking-tight">
                            Nuestra ubicación:
                        </h2>
                        <div className="w-full h-[400px] overflow-hidden border rounded-lg">
                            <StoreMap />
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}
