"use client";

import { useMemo, useState } from "react";
import Swal from "sweetalert2";

import AdminPageHeader from "@/organisms/AdminPageHeader";
import AdminTableCard from "@/organisms/AdminTableCard";
import AdminPageTemplate from "@/templates/AdminPageTemplate";
import AdminProductModal from "@/organisms/AdminProductModal";

import Pagination from "@/molecules/Pagination";
import { FetchData, useDeleteData, usePaginateData } from "@/hooks/useClientData";

const PER_PAGE = 10;

export default function AdminProductsPage() {
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(1);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState("view");
    const [selectedProduct, setSelectedProduct] = useState(null);

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.set("page", page.toString());
        params.set("perPage", PER_PAGE.toString());
        if (search) params.set("search", search);
        return `/products?${params.toString()}`;
    }, [page, search]);

    const { data: products, total, loading, reload } = usePaginateData(endpoint);

    const handleSearch = (value) => {
        setSearch(value);
        setPage(1);
    };

    const openModal = async (mode, productId = null) => {
        setModalMode(mode);
        if (mode === "create") {
            setSelectedProduct(null);
            setModalOpen(true);
            return;
        }

        const response = await FetchData(`/products/${productId}`);
        if (response?.success) {
            setSelectedProduct(response.data);
            setModalOpen(true);
        }
    };

    const handleDelete = async (productId) => {
        Swal.fire({
            icon: "warning",
            title: "¿Eliminar producto?",
            text: "Esta acción no se puede deshacer",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#d33",
        }).then(async (result) => {
            if (result.isConfirmed) {
                const response = await useDeleteData(`/products/${productId}`);
                if (response?.success) {
                    reload();
                }
            }
        });
    };

    const rows = products?.length
        ? products.map((product) => (
              <tr key={product.id} className="text-sm">
                  <td>{product.id}</td>
                  <td>{product.name}</td>
                  <td>${product.price}</td>
                  <td>{product.discount}%</td>
                  <td>{product.condition}</td>
                  <td>{product.createdAt ? new Date(product.createdAt).toLocaleDateString() : "—"}</td>
                  <td className="flex flex-col gap-2">
                      <button
                          className="btn btn-xs btn-outline"
                          onClick={() => openModal("view", product.id)}
                      >
                          Ver
                      </button>
                      <button
                          className="btn btn-xs btn-primary"
                          onClick={() => openModal("edit", product.id)}
                      >
                          Editar
                      </button>
                      <button
                          className="btn btn-xs btn-ghost text-red-400"
                          onClick={() => handleDelete(product.id)}
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
                    title="Administrar productos"
                    action={
                        <button
                            className="btn btn-primary btn-outline"
                            onClick={() => openModal("create")}
                        >
                            + Crear Producto
                        </button>
                    }
                />
                <AdminTableCard
                    title="Productos"
                    searchPlaceholder="Buscar productos"
                    searchValue={search}
                    onSearchChange={handleSearch}
                    columns={[
                        "ID",
                        "Nombre",
                        "Precio",
                        "Descuento",
                        "Tipo",
                        "Fecha",
                        "Acciones",
                    ]}
                    emptyMessage="No hay productos..."
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
            <AdminProductModal
                open={modalOpen}
                mode={modalMode}
                product={selectedProduct}
                onClose={() => setModalOpen(false)}
                onSaved={reload}
            />
        </AdminPageTemplate>
    );
}
