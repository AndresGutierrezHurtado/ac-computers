"use client";

import { useMemo, useState } from "react";
import Swal from "sweetalert2";

import AdminPageHeader from "@/organisms/AdminPageHeader";
import AdminTableCard from "@/organisms/AdminTableCard";
import AdminPageTemplate from "@/templates/AdminPageTemplate";
import AdminUserModal from "@/organisms/AdminUserModal";

import Pagination from "@/molecules/Pagination";
import { FetchData, useDeleteData, usePaginateData } from "@/hooks/useClientData";

const PER_PAGE = 10;

export default function AdminUsersPage() {
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(1);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState("view");
    const [selectedUser, setSelectedUser] = useState(null);

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.set("page", page.toString());
        params.set("perPage", PER_PAGE.toString());
        if (search) params.set("search", search);
        return `/users?${params.toString()}`;
    }, [page, search]);

    const { data: users, total, loading, reload } = usePaginateData(endpoint);

    const handleSearch = (value) => {
        setSearch(value);
        setPage(1);
    };

    const openModal = async (mode, userId = null) => {
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
    };

    const handleDelete = async (userId) => {
        Swal.fire({
            icon: "warning",
            title: "¿Eliminar usuario?",
            text: "Esta acción no se puede deshacer",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#d33",
        }).then(async (result) => {
            if (result.isConfirmed) {
                const response = await useDeleteData(`/users/${userId}`);
                if (response?.success) {
                    reload();
                }
            }
        });
    };

    const rows = users?.length
        ? users.map((user) => (
              <tr key={user.id} className="text-sm">
                  <td>{user.id}</td>
                  <td>{`${user.firstName} ${user.lastName}`}</td>
                  <td>{user.email}</td>
                  <td>{user.role?.name || "—"}</td>
                  <td className="flex flex-col gap-2">
                      <button
                          className="btn btn-xs btn-outline"
                          onClick={() => openModal("view", user.id)}
                      >
                          Ver
                      </button>
                      <button
                          className="btn btn-xs btn-primary"
                          onClick={() => openModal("edit", user.id)}
                      >
                          Editar
                      </button>
                      <button
                          className="btn btn-xs btn-ghost text-red-400"
                          onClick={() => handleDelete(user.id)}
                      >
                          Eliminar
                      </button>
                  </td>
              </tr>
          ))
        : null;

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
                            + Crear Usuario
                        </button>
                    }
                />
                <AdminTableCard
                    title="Usuarios"
                    searchPlaceholder="Buscar usuarios"
                    searchValue={search}
                    onSearchChange={handleSearch}
                    columns={["ID", "Nombres", "Correo Electrónico", "Rol", "Acciones"]}
                    emptyMessage="No hay usuarios..."
                    loading={loading}
                >
                    {rows}
                </AdminTableCard>
                {total ? (
                    <Pagination
                        page={page}
                        count={total}
                        limit={PER_PAGE}
                        onPageChange={setPage}
                    />
                ) : null}
            </div>
            <AdminUserModal
                open={modalOpen}
                mode={modalMode}
                user={selectedUser}
                onClose={() => setModalOpen(false)}
                onSaved={reload}
            />
        </AdminPageTemplate>
    );
}
