import Link from "next/link";

export default function Login() {
    return (
        <>
            <div className="hero bg-base-200 min-h-screen">
                <div className="hero-content flex-col lg:flex-row-reverse gap-[50px] z-[1]">
                    <div className="text-center lg:text-left flex flex-col items-center lg:items-start gap-4">
                        <h1 className="text-4xl font-extrabold text-nowrap">
                            ¿No has creado una cuenta?
                        </h1>
                        <p className="pb-3 text-balance text-lg max-w-lg">
                            Si aún no tienes una cuenta, puedes crearla en el siguiente botón
                        </p>
                        <Link href="/register">
                            <button className="btn btn-primary btn-outline btn-wide font-medium">
                                Registrarse
                            </button>
                        </Link>
                    </div>
                    <div className="card bg-base-100 w-full max-w-[500px] shrink-0 shadow-2xl">
                        <div className="card-body flex flex-col gap-2 p-10 px-7">
                            <div>
                                <Link href="/">
                                    <h1 className="text-4xl font-extrabold text-center text-primary">
                                        AC COMPUTERS
                                    </h1>
                                </Link>
                                <p className="text-center text-2xl font-medium">Iniciar Sesión</p>
                            </div>
                            <form>
                                <fieldset className="fieldset gap-4">
                                    <div className="fieldset">
                                        <label className="fieldset-label font-medium text-base">
                                            Correo electrónico:
                                        </label>
                                        <input
                                            className="input w-full focus:outline-0 focus:border-primary"
                                            placeholder="Ingresa tu correo electrónico"
                                            name="user_email"
                                        />
                                    </div>
                                    <div className="fieldset">
                                        <label className="fieldset-label font-medium text-base">
                                            Contraseña:
                                        </label>
                                        <input
                                            type="password"
                                            className="input w-full focus:outline-0 focus:border-primary"
                                            placeholder="Ingresa tu contraseña"
                                            name="user_password"
                                        />
                                    </div>
                                    <div>
                                        <Link
                                            href="/forgot"
                                            className="link link-hover text-primary font-medium text-base"
                                        >
                                            Olvidaste tu contraseña?
                                        </Link>
                                    </div>
                                    <button className="btn btn-primary font-medium mt-4">
                                        Iniciar Sesión
                                    </button>
                                </fieldset>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
