import React from "react";
import Link from "next/link";
import Pagination from "../../components/pagination";
import { DownloadIcon, PriceTagsIcon, SearchIcon } from "@/components/icons";

export const metadata = {
    title: "Computadores | AC Computers",
};

export const dynamic = "force-dynamic";

export default async function Page({ searchParams }) {
    const { page = 1, search = "" } = await searchParams;

    const response = await fetch(
        `${process.env.APP_DOMAIN}/api/products?type=1&limit=8&page=${page}&search=${search}`
    );
    const { data: computers, limit, count } = await response.json();

    return (
        <main className="w-full">
            <section className="w-full px-3 mt-[100px]">
                <div className="w-full max-w-[1200px] mx-auto flex flex-col gap-10">
                    <div className="flex flex-col sm:flex-row items-start sm:items-end gap-4 justify-between">
                        <h2 className="text-4xl font-extrabold tracking-tight">
                            Lista computadores:
                        </h2>
                        <form action="/computers" method="get" className="w-full max-w-sm">
                            <label className="input input-sm input-bordered focus-within:outline-0 focus-within:input-primary flex items-center gap-2 w-full">
                                <input
                                    type="text"
                                    className="grow group"
                                    placeholder="Buscar computadores"
                                    name="search"
                                    defaultValue={search}
                                />
                                <button type="submit">
                                    <SearchIcon className="opacity-70 w-4 h-4" />
                                </button>
                            </label>
                        </form>
                    </div>
                    <div className="flex justify-center gap-4 flex-wrap">
                        <Link
                            href={`${process.env.APP_DOMAIN}/api/products/pdf?type=1`}
                            target="_blank"
                            className="btn btn-primary w-fit px-5 btn-outline btn-sm py-2 h-auto flex-nowrap"
                        >
                            <DownloadIcon className="text-xl" />
                            Descargar lista PDF de computadores
                        </Link>
                        <Link
                            href={`${process.env.APP_DOMAIN}/api/products/pdf`}
                            target="_blank"
                            className="btn btn-primary w-fit px-5 btn-sm py-2 h-auto flex-nowrap"
                        >
                            <PriceTagsIcon className="text-xl" />
                            Descargar lista de precios
                        </Link>
                    </div>

                    {computers.length === 0 && (
                        <section className="flex items-center justify-center flex-col gap-2 text-center">
                            <h1 className="text-7xl tracking-tight font-extrabold lg:text-9xl text-primary">
                                404
                            </h1>
                            <p className="mb-4 text-3xl tracking-tight font-bold text-white">
                                No se encuentran productos
                            </p>
                        </section>
                    )}

                    <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-14">
                        {computers.map((product) => (
                            <div
                                className="card rounded-none [&_p]:grow-0"
                                key={product.product_id}
                            >
                                <Link
                                    href={`/product/${product.product_id}`}
                                    className="w-full aspect-square"
                                >
                                    <img
                                        src={product.product_image_url}
                                        alt={product.product_name}
                                        className="w-full h-full object-contain"
                                    />
                                </Link>
                                <div className="card-body p-2 gap-0 text-center">
                                    <p>AC Computers</p>
                                    <Link
                                        href={`/product/${product.product_id}`}
                                        className="font-bold text-lg leading-none tracking-tight hover:text-gray-400 hover:underline"
                                    >
                                        {product.product_name}
                                    </Link>
                                    <div className="flex justify-center gap-2">
                                        {product.product_discount > 0 && (
                                            <p className="line-through text-gray-400">
                                                {parseInt(product.product_price).toLocaleString(
                                                    "es-CO",
                                                    {
                                                        style: "currency",
                                                        currency: "COP",
                                                    }
                                                )}
                                            </p>
                                        )}
                                        <p>
                                            {parseInt(
                                                product.product_price *
                                                    (1 - product.product_discount / 100)
                                            ).toLocaleString("es-CO", {
                                                style: "currency",
                                                currency: "COP",
                                            })}
                                        </p>
                                    </div>
                                </div>
                                {product.product_discount > 0 && (
                                    <div className="absolute top-2 right-2">
                                        <div className="w-fit px-2 h-9 bg-primary rounded-full flex items-center justify-center text-sm font-semibold text-white">
                                            {product.product_discount}%
                                        </div>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                    <div className="w-full flex justify-center">
                        <Pagination
                            url={"/computers"}
                            page={parseInt(page)}
                            count={count}
                            limit={limit}
                        />
                    </div>
                </div>
            </section>
        </main>
    );
}
