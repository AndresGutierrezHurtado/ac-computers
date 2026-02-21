export default function ProfilePage() {
    return (
        <div className="w-full px-4">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-25">
                <div className="w-full max-w-4xl mx-auto p-5 bg-base-200 border border-base-300 rounded-lg space-y-4">
                    <div className="flex items-center justify-between">
                        <h2 className="text-2xl font-bold">Información personal</h2>
                    </div>
                    <div className="flex gap-10">
                        <div className="space-y-2 flex-1">
                            <label className="fieldset-label text-sm after:content-['*'] after:text-red-500">
                                Nombre completo
                            </label>
                            <p>—</p>
                        </div>
                        <div className="space-y-2 flex-1">
                            <label className="fieldset-label text-sm after:content-['*'] after:text-red-500">
                                Correo electrónico
                            </label>
                            <p>—</p>
                        </div>
                        <div className="space-y-2 flex-1">
                            <label className="block text-base-content/70 font-semibold">
                                Teléfono
                            </label>
                            <p>—</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
