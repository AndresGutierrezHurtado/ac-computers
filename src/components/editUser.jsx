"use client";
import React from "react";

// Hooks
import { usePutData } from "@/hooks/useClientData.js";
import { useValidateform } from "@/hooks/useValidateForm.js";

// Components
import { UploadIcon } from "./icons.jsx";

export default function EditUser({ user, userSession, reloadUsers }) {
    const handleFormSubmit = async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(e.target));
        const validation = useValidateform({role_id: user.role_id.toString(), ...data}, "update-user-form");

        if (validation.success) {
            const response = await usePutData(`/users/${user.user_id}`, { user: data });

            e.target.closest("dialog").close();
            if (response.success) {
                reloadUsers();
            }
        }
    };

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
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Nombre:
                                </span>
                            </label>
                            <input
                                name="user_name"
                                placeholder="Ingresa tu nombre"
                                defaultValue={user.user_name}
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Apellidos:
                                </span>
                            </label>
                            <input
                                name="user_lastname"
                                placeholder="Ingresa tus apellidos"
                                defaultValue={user.user_lastname}
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Correo Electrónico:
                                </span>
                            </label>
                            <input
                                placeholder="correo@ejemplo.com"
                                defaultValue={user.user_email}
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                                disabled
                            />
                        </fieldset>
                        <fieldset className="fieldset">
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Teléfono:
                                </span>
                            </label>
                            <input
                                name="user_phone"
                                placeholder="Ingresa tu numero de teléfono"
                                defaultValue={user.user_phone}
                                className="input input-sm input-bordered focus:input-primary focus:outline-0 w-full"
                            />
                        </fieldset>
                        <fieldset
                            className="fieldset"
                            disabled={userSession.user_id === user.user_id}
                        >
                            <label className="label">
                                <span className="label-text font-semibold after:content-['*'] after:text-red-500 after:ml-0.5">
                                    Rol:
                                </span>
                            </label>
                            <select
                                name="role_id"
                                defaultValue={user.role_id}
                                className="select select-sm select-bordered focus:select-primary focus:outline-0 w-full"
                            >
                                <option value="1">Usuario</option>
                                <option value="2">Administrador</option>
                            </select>
                        </fieldset>
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
