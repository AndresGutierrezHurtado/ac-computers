"use client";

import { useState } from "react";
import { DownloadIcon } from "@/atoms/icons";
import Link from "next/link";
import { downloadCatalogPdf } from "@/utils/downloadCatalog";

export default function CatalogButtons({ listHref, listLabel, pdfEndpoint }) {
    const [downloading, setDownloading] = useState(false);

    const handleDownload = async () => {
        if (downloading) return;
        setDownloading(true);
        try {
            await downloadCatalogPdf(pdfEndpoint);
        } catch (error) {
            console.error(error);
        } finally {
            setDownloading(false);
        }
    };

    return (
        <div className="flex gap-4">
            <Link href={listHref}>
                <button className="btn btn-outline border-primary text-primary hover:bg-primary/10 w-fit rounded-lg font-medium">
                    {listLabel}
                </button>
            </Link>
            <button
                className="btn bg-primary hover:bg-primary/80 w-fit text-black rounded-lg font-medium"
                onClick={handleDownload}
                type="button"
                disabled={downloading}
            >
                <DownloadIcon size={18} />
                {downloading ? "Generando..." : "Descargar Catalogo"}
            </button>
        </div>
    );
}
