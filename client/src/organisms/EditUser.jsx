"use client";
import React from "react";

// Hooks
import { usePutData } from "@/hooks/useClientData.js";
import { useValidateform } from "@/hooks/useValidateForm.js";

// Components
import { UploadIcon } from "@/atoms/Icons";
import TextField from "@/molecules/TextField";
import SelectField from "@/molecules/SelectField";

const ROLE_OPTIONS = [
    { value: "1", label: "Usuario" },
    { value: "2", label: "Administrador" },
];

export default function EditUser({ user, userSession, reloadUsers }) {
    const handleFormSubmit = async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));
        const validation = useValidateform(
            { role_id: user.role_id.toString(), ...data },
            "update-user-form",
        );

        if (validation.success) {
            const response = await usePutData(`/users/${user.user_id}`, { user: data });

            e.target.closest("dialog").close();
            if (response.success) {
                reloadUsers();
            }
        }
    };

    const inputClass = "input-sm input-bordered focus:input-primary focus:outline-0 w-full";
    const selectClass = "select-sm select-bordered focus:select-primary focus:outline-0 w-full";
    const roleDisabled = userSession.user_id === user.user_id;

    return (
        <>
            <dialog id={`edit-user-${user.user_id}`} className="modal pr-0 mr-0">
                <div className="modal-box">
                    <div className="modal-dialog">
                        <form method="dialog">
                            <button
                                id={`close-button-${user.user_id}`}
                                className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
                            >
                                ✕
                            </button>
                        </form>
                    </div>
                    <h3 className="font-extrabold text-2xl tracking-tight">Edita tu usuario:</h3>
                    <p className="py-4">
                        Para cerrar presiona <kbd className="kbd kbd-sm">Esc</kbd> o haz click fuera
                        de la ventana modal.
                    </p>
                    <form onSubmit={handleFormSubmit} className="space-y-2">
                        <TextField
                            label="Nombre:"
                            name="user_name"
                            placeholder="Ingresa tu nombre"
                            defaultValue={user.user_name}
                            required
                            className={inputClass}
                        />
                        <TextField
                            label="Apellidos:"
                            name="user_lastname"
                            placeholder="Ingresa tus apellidos"
                            defaultValue={user.user_lastname}
                            required
                            className={inputClass}
                        />
                        <TextField
                            label="Correo Electrónico:"
                            placeholder="correo@ejemplo.com"
                            defaultValue={user.user_email}
                            disabled
                            className={inputClass}
                        />
                        <TextField
                            label="Teléfono:"
                            name="user_phone"
                            placeholder="Ingresa tu numero de teléfono"
                            defaultValue={user.user_phone}
                            required
                            className={inputClass}
                        />
                        <SelectField
                            label="Rol:"
                            name="role_id"
                            defaultValue={String(user.role_id)}
                            options={ROLE_OPTIONS}
                            disabled={roleDisabled}
                            required
                            className={selectClass}
                        />
                        <div className="form-control pt-5">
                            <button className="btn btn-primary btn-sm w-full">
                                <UploadIcon size={20} />
                                Subir
                            </button>
                        </div>
                    </form>
                </div>
                <form method="dialog" className="modal-backdrop bg-black/50 backdrop-blur-[1px]">
                    <button>close</button>
                </form>
            </dialog>
        </>
    );
}
