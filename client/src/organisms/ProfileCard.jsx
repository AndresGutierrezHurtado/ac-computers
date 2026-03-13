import TextField from "@/molecules/TextField";

export default function ProfileCard({ user, loading }) {
    const fullName = user ? `${user.firstName || ""} ${user.lastName || ""}`.trim() : "";

    return (
        <div className="w-full max-w-4xl mx-auto p-5 bg-base-200 border border-base-300 rounded-lg space-y-4">
            <div className="flex items-center justify-between">
                <h2 className="text-2xl font-bold">Información personal</h2>
            </div>
            <div className="flex flex-col md:flex-row gap-6">
                <div className="flex-1 min-w-0">
                    <TextField
                        label="Nombre completo"
                        value={loading ? "Cargando..." : fullName}
                        disabled
                    />
                </div>
                <div className="flex-1 min-w-0">
                    <TextField
                        label="Correo electrónico"
                        value={loading ? "Cargando..." : user?.email}
                        type="email"
                        disabled
                    />
                </div>
                <div className="flex-1 min-w-0">
                    <TextField
                        label="Rol"
                        value={loading ? "Cargando..." : user?.role?.name}
                        disabled
                    />
                </div>
            </div>
        </div>
    );
}
