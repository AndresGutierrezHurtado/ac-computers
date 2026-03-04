import ProfileField from "@/molecules/ProfileField";

export default function ProfileCard({ user, loading }) {
    const fullName = user ? `${user.firstName || ""} ${user.lastName || ""}`.trim() : "";

    return (
        <div className="w-full max-w-4xl mx-auto p-5 bg-base-200 border border-base-300 rounded-lg space-y-4">
            <div className="flex items-center justify-between">
                <h2 className="text-2xl font-bold">Información personal</h2>
            </div>
            <div className="flex flex-col md:flex-row gap-6">
                <ProfileField
                    label="Nombre completo"
                    value={loading ? "Cargando..." : fullName}
                    required
                />
                <ProfileField
                    label="Correo electrónico"
                    value={loading ? "Cargando..." : user?.email}
                    required
                />
                <ProfileField
                    label="Rol"
                    value={loading ? "Cargando..." : user?.role?.name}
                />
            </div>
        </div>
    );
}
