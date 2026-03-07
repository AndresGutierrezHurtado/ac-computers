"use client";

import { useEffect, useState } from "react";

import Modal from "@/molecules/Modal";
import AdminFormField from "@/molecules/AdminFormField";
import AdminSelectField from "@/molecules/AdminSelectField";

import { usePostData, usePutData } from "@/hooks/useClientData";
import { useValidateform } from "@/hooks/useValidateForm";

const ROLE_OPTIONS = [
    { value: "1", label: "Superuser" },
    { value: "2", label: "Administrador" },
    { value: "3", label: "Viewer" },
];

const emptyForm = {
    firstName: "",
    lastName: "",
    email: "",
    roleId: "3",
    password: "",
};

export default function AdminUserModal({
    open,
    mode = "view",
    user,
    onClose,
    onSaved,
    onEdit,
    onDelete,
}) {
    const [form, setForm] = useState(emptyForm);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        if (!open) return;
        if (!user) {
            setForm(emptyForm);
            return;
        }

        setForm({
            firstName: user.firstName || "",
            lastName: user.lastName || "",
            email: user.email || "",
            roleId: user.role?.id?.toString() || "3",
            password: "",
        });
    }, [open, user]);

    const readOnly = mode === "view";

    const handleChange = (field) => (event) => {
        setForm((prev) => ({ ...prev, [field]: event.target.value }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (readOnly || submitting) return;
        setSubmitting(true);

        const validation = useValidateform(
            {
                firstName: form.firstName,
                lastName: form.lastName,
                email: form.email,
                roleId: form.roleId,
                ...(mode === "create" ? { password: form.password } : {}),
            },
            mode === "create" ? "admin-create-user-form" : "admin-update-user-form",
        );

        if (!validation.success) {
            setSubmitting(false);
            return;
        }

        const response =
            mode === "create"
                ? await usePostData("/auth/register", {
                      firstName: form.firstName,
                      lastName: form.lastName,
                      email: form.email,
                      password: form.password,
                      roleId: Number(form.roleId),
                  })
                : await usePutData(`/users/${user.id}`, {
                      firstName: form.firstName,
                      lastName: form.lastName,
                      email: form.email,
                      roleId: Number(form.roleId),
                  });

        if (response?.success) {
            onSaved?.();
            onClose();
        }

        setSubmitting(false);
    };

    if (open && mode !== "create" && !user) {
        return (
            <Modal title="Detalle usuario" open={open} onClose={onClose}>
                <p className="text-center">Cargando...</p>
            </Modal>
        );
    }

    const footer =
        readOnly && user ? (
            <div className="flex flex-col-reverse sm:flex-row justify-end gap-2">
                <button
                    type="button"
                    className="btn btn-ghost text-red-400"
                    onClick={async () => {
                        if (!onDelete) return;
                        const removed = await onDelete(user.id);
                        if (removed) {
                            onClose();
                        }
                    }}
                >
                    Eliminar
                </button>
                <button
                    type="button"
                    className="btn btn-primary"
                    onClick={() => onEdit?.(user.id)}
                >
                    Editar
                </button>
            </div>
        ) : null;

    return (
        <Modal
            title={
                mode === "create" ? "Crear usuario" : mode === "edit" ? "Editar usuario" : "Detalle usuario"
            }
            open={open}
            onClose={onClose}
            footer={footer}
        >
            <form onSubmit={handleSubmit} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <AdminFormField
                        label="Nombre"
                        name="firstName"
                        placeholder="Nombre"
                        value={form.firstName}
                        onChange={handleChange("firstName")}
                        disabled={readOnly}
                    />
                    <AdminFormField
                        label="Apellido"
                        name="lastName"
                        placeholder="Apellido"
                        value={form.lastName}
                        onChange={handleChange("lastName")}
                        disabled={readOnly}
                    />
                </div>
                <AdminFormField
                    label="Correo electrónico"
                    name="email"
                    placeholder="correo@ejemplo.com"
                    value={form.email}
                    onChange={handleChange("email")}
                    disabled={readOnly}
                />
                <AdminSelectField
                    label="Rol"
                    name="roleId"
                    value={form.roleId}
                    onChange={handleChange("roleId")}
                    options={ROLE_OPTIONS}
                    disabled={readOnly}
                />
                {mode === "create" ? (
                    <AdminFormField
                        label="Contraseña"
                        name="password"
                        type="password"
                        placeholder="Contraseña"
                        value={form.password}
                        onChange={handleChange("password")}
                        disabled={readOnly}
                    />
                ) : null}
                {!readOnly ? (
                    <button className="btn btn-primary w-full" disabled={submitting}>
                        {submitting ? "Guardando..." : "Guardar"}
                    </button>
                ) : null}
            </form>
        </Modal>
    );
}
