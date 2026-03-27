"use client";

import { useCallback, useMemo, useState } from "react";
import Swal from "sweetalert2";

import { PlusIcon } from "@/atoms/Icons";
import Pagination from "@/molecules/Pagination";
import AdminPageHeader from "@/organisms/AdminPageHeader";
import AdminTableCard from "@/organisms/AdminTableCard";
import AdminUserModal from "@/organisms/AdminUserModal";
import AdminPageTemplate from "@/templates/AdminPageTemplate";

import { FetchData, useDeleteData, usePaginateData } from "@/hooks/useClientData";
import { usePageTitle } from "@/hooks/usePageTitle";

export default function AdminUsersPage() {
    usePageTitle("Administrar usuarios");

    const [search, setSearch] = useState("");
    const [page, setPage] = useState(1);
    const [limit, setLimit] = useState(5);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState("view");
    const [selectedUser, setSelectedUser] = useState(null);

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.set("page", page.toString());
        params.set("perPage", limit.toString());
        if (search) params.set("search", search);
        return `/users?${params.toString()}`;
    }, [page, limit, search]);

    const { data: users, total, loading, reload } = usePaginateData(endpoint);

    const handleSearch = useCallback((value) => {
        setSearch(value);
        setPage(1);
    }, []);

    const handleLimitChange = useCallback((next) => {
        setLimit(next);
        setPage(1);
    }, []);

    const openModal = useCallback(async (mode, userId = null) => {
        setModalMode(mode);
        if (mode === "create") {
            setSelectedUser(null);
            setModalOpen(true);
            return;
        }

        const response = await FetchData(`/users/${userId}`);
        if (response?.success) {
            setSelectedUser(response.data);
            setModalOpen(true);
        }
    }, []);

    const handleDelete = useCallback(async (userId) => {
        const result = await Swal.fire({
            icon: "warning",
            title: "¿Eliminar usuario?",
            text: "Esta acción no se puede deshacer",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: "Cancelar",
        });

        if (!result.isConfirmed) return false;

        const response = await useDeleteData(`/users/${userId}`);
        if (response?.success) {
            reload();
            return true;
        }

        return false;
    }, []);

    const rows = useMemo(() => {
        if (!users?.length) return null;

        return users.map((user) => (
            <tr
                key={user.id}
                className="text-sm cursor-pointer transition-colors hover:bg-zinc-950/40"
                onDoubleClick={() => openModal("view", user.id)}
            >
                <td>{user.id}</td>
                <td>{`${user.firstName} ${user.lastName}`}</td>
                <td>{user.email}</td>
                <td>{user.role?.name || "—"}</td>
            </tr>
        ))
    }, [users, openModal]);

    return (
        <AdminPageTemplate>
            <div className="space-y-5">
                <AdminPageHeader
                    title="Administrar usuarios"
                    action={
                        <button
                            className="btn btn-primary btn-outline"
                            onClick={() => openModal("create")}
                        >
                            <PlusIcon size={16} /> Crear Usuario
                        </button>
                    }
                />
                <AdminTableCard
                    title="Usuarios"
                    searchPlaceholder="Buscar usuarios"
                    searchValue={search}
                    onSearchChange={handleSearch}
                    columns={["ID", "Nombres", "Correo Electrónico", "Rol"]}
                    emptyMessage="No hay usuarios..."
                    loading={loading}
                >
                    {rows}
                </AdminTableCard>
                {total ? (
                    <Pagination
                        page={page}
                        count={total}
                        limit={limit}
                        onPageChange={setPage}
                        onLimitChange={handleLimitChange}
                    />
                ) : null}
            </div>
            <AdminUserModal
                open={modalOpen}
                mode={modalMode}
                user={selectedUser}
                onClose={() => setModalOpen(false)}
                onSaved={reload}
                onEdit={(userId) => openModal("edit", userId)}
                onDelete={handleDelete}
            />
        </AdminPageTemplate>
    );
}
