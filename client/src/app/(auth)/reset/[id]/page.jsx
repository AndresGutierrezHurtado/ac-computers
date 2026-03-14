import TextField from "@/molecules/TextField";

export default function ResetPage() {
    return (
        <>
            <section className="w-full px-4 mt-[100px]">
                <div className="w-full max-w-xl mx-auto py-10 space-y-10">
                    <h2 className="text-4xl font-extrabold uppercase text-center">
                        AC <span className="text-primary italic">Computers</span>
                    </h2>
                    <div className="w-full p-5 rounded-lg bg-base-200 border border-base-300 shadow-lg shadow-base-300/30 space-y-5">
                        <div className="space-y-2">
                            <h2 className="text-3xl font-bold">Cambiar contraseña</h2>
                            <p className="text-base-content/80">
                                Ingresa tu nueva contraseña para cambiarla.
                            </p>
                        </div>
                        <form className="w-full space-y-4">
                            <TextField
                                label="Nueva contraseña"
                                name="user_password"
                                type="password"
                                placeholder="Ingresa tu nueva contraseña"
                                required
                                className="input-bordered w-full"
                            />
                            <div className="w-full">
                                <button
                                    type="submit"
                                    className="btn btn-primary btn-wide font-medium"
                                >
                                    Cambiar contraseña
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </section>
        </>
    );
}
