export default function ContactPage() {
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
                                    ¿Tienes alguna pregunta o comentario? No dudes en escribirnos.
                                    Estamos aquí para ayudarte.
                                </p>
                            </div>
                        </div>
                        <div className="card bg-black/20 h-fit w-full md:w-1/2">
                            <div className="card-body [&_p]:grow-0 px-8 py-10">
                                <form className="space-y-2">
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
                                            Enviar mensaje
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}
