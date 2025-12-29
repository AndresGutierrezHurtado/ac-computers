"use client";

import { useSession } from "next-auth/react";
import React, { useEffect, useState } from "react";

// Components
import Loading from "@/components/loading";
import { useValidateform } from "@/hooks/useValidateForm";
import { usePutData } from "@/hooks/useClientData";
import { CloseIcon, EditIcon } from "@/components/icons";

export default function ProfilePage() {
    const [canEdit, setCanEdit] = useState(false);
    const { data, status } = useSession();
    const user = data?.user;

    useEffect(() => {
        document.title = "Perfil | Mi cuenta";
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));

        const validation = useValidateform(data, "update-user-form");
        console.log(validation);
        if (!validation.success) return;

        const response = usePutData(`/users/${user.user_id}`, { user: data });
        if (!response.success) return;

        setCanEdit(false);
    };

    if (status === "loading") return <Loading />;

    return (
        <div className="w-full px-4">
            <div className="w-full max-w-[1200px] mx-auto py-10 mt-25">
                <div className="w-full max-w-4xl mx-auto p-5 bg-base-200 border border-base-300 rounded-lg space-y-4">
                    <div className="flex items-center justify-between">
                        <h2 className="text-2xl font-bold">Información personal</h2>
                        <button
                            className="btn btn-outline h-auto py-1"
                            onClick={() => setCanEdit((prev) => !prev)}
                        >
                            {canEdit ? <CloseIcon /> : <EditIcon />}
                            {canEdit ? "Cancelar" : "Editar"}
                        </button>
                    </div>
                    <div className="flex gap-10">
                        <div className="space-y-2 flex-1">
                            <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                Nombre completo
                            </label>
                            <p>{`${user.user_name} ${user.user_lastname}`}</p>
                        </div>
                        <div className="space-y-2 flex-1">
                            <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                Correo electrónico
                            </label>
                            <p>{user.user_email}</p>
                        </div>
                        <div className="space-y-2 flex-1">
                            <label className="block text-base-content/70 font-semibold">
                                Teléfono
                            </label>
                            <p>{user.user_phone}</p>
                        </div>
                    </div>
                    {canEdit && (
                        <>
                            <hr className="my-8 border border-base-300" />
                            <form onSubmit={handleSubmit} className="space-y-2">
                                <input type="hidden" name="role_id" defaultValue={user.role_id} />
                                <h2 className="text-2xl font-bold mb-6">Editar Perfil</h2>

                                <fieldset className="w-full fieldset">
                                    <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                        Nombre
                                    </label>
                                    <input
                                        defaultValue={user.user_name}
                                        className="w-full input input-bordered focus:outline-0 focus:border-primary"
                                        name="user_name"
                                    />
                                </fieldset>

                                <fieldset className="w-full fieldset">
                                    <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                        Apellidos
                                    </label>
                                    <input
                                        defaultValue={user.user_lastname}
                                        className="w-full input input-bordered focus:outline-0 focus:border-primary"
                                        name="user_lastname"
                                    />
                                </fieldset>

                                <fieldset className="w-full fieldset">
                                    <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                        Correo electrónico
                                    </label>
                                    <input
                                        defaultValue={user.user_email}
                                        className="w-full input input-bordered focus:outline-0 focus:border-primary"
                                        name="user_email"
                                    />
                                </fieldset>

                                <fieldset className="w-full fieldset">
                                    <label className="fieldset-label text-sm  after:content-['*'] after:text-red-500">
                                        Teléfono
                                    </label>
                                    <input
                                        defaultValue={user.user_phone}
                                        className="w-full input input-bordered focus:outline-0 focus:border-primary"
                                        name="user_phone"
                                    />
                                </fieldset>

                                <div className="mt-8 flex justify-end gap-4">
                                    <button type="reset" className="btn btn-outline">
                                        <span className="text-base-content/70">Reiniciar</span>
                                    </button>
                                    <button className="btn btn-primary">Guardar cambios</button>
                                </div>
                            </form>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}
