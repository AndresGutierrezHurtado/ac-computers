"use client";

import { useCallback, useMemo, useState } from "react";
import Swal from "sweetalert2";

import AdminPageHeader from "@/organisms/AdminPageHeader";
import AdminTableCard from "@/organisms/AdminTableCard";
import AdminPageTemplate from "@/templates/AdminPageTemplate";
import AdminProductModal from "@/organisms/AdminProductModal";

import Pagination from "@/molecules/Pagination";
import { FetchData, useDeleteData, usePaginateData } from "@/hooks/useClientData";

export default function AdminProductsPage() {
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(1);
    const [limit, setLimit] = useState(5);
    const [modalOpen, setModalOpen] = useState(false);
    const [modalMode, setModalMode] = useState("view");
    const [selectedProduct, setSelectedProduct] = useState(null);

    const endpoint = useMemo(() => {
        const params = new URLSearchParams();
        params.set("page", page.toString());
        params.set("perPage", limit.toString());
        if (search) params.set("search", search);
        return `/products?${params.toString()}`;
    }, [page, limit, search]);

    const { data: products, total, loading, reload } = usePaginateData(endpoint);

    const handleSearch = useCallback((value) => {
        setSearch(value);
        setPage(1);
    }, []);

    const handleLimitChange = useCallback((next) => {
        setLimit(next);
        setPage(1);
    }, []);

    const openModal = useCallback(async (mode, productId = null) => {
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
    }, []);

    const handleDelete = useCallback(async (productId) => {
        const result = await Swal.fire({
            icon: "warning",
            title: "¿Eliminar producto?",
            text: "Esta acción no se puede deshacer",
            showCancelButton: true,
            confirmButtonText: "Eliminar",
            cancelButtonText: "Cancelar",
        });

        if (!result.isConfirmed) return false;

        const response = await useDeleteData(`/products/${productId}`);
        if (response?.success) {
            reload();
            return true;
        }

        return false;
    }, []);

    const getConditionLabel = useCallback((condition) => {
        switch (condition) {
            case "new":
                return "Nuevo";
            case "used":
                return "Usado";
            case "refurbished":
                return "Reacondicionado";
            case "for_parts":
                return "Para repuestos";
            default:
                return "—";
        }
    }, []);

    const rows = useMemo(() => {
        if (!products?.length) return null;

        return products.map((product) => (
            <tr
                key={product.id}
                className="text-sm cursor-pointer transition-colors hover:bg-zinc-950/40"
                onDoubleClick={() => openModal("view", product.id)}
            >
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>COP {product.price.toLocaleString()}</td>
                <td>{product.discount}%</td>
                <td>{getConditionLabel(product.condition)}</td>
                <td>{product.createdAt ? new Date(product.createdAt).toLocaleDateString() : "—"}</td>
            </tr>
        ));
    }, [products, openModal]);

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
                        limit={limit}
                        onPageChange={setPage}
                        onLimitChange={handleLimitChange}
                    />
                ) : null}
            </div>
            <AdminProductModal
                open={modalOpen}
                mode={modalMode}
                product={selectedProduct}
                onClose={() => setModalOpen(false)}
                onSaved={reload}
                onEdit={(productId) => openModal("edit", productId)}
                onDelete={handleDelete}
            />
        </AdminPageTemplate>
    );
}
