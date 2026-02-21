import { DownloadIcon } from "@/atoms/icons";
import Link from "next/link";

export default function CatalogButtons({ listHref, listLabel, pdfHref }) {
    return (
        <div className="flex gap-4">
            <Link href={listHref}>
                <button className="btn btn-outline border-primary text-primary hover:bg-primary/10 w-fit rounded-lg font-medium">
                    {listLabel}
                </button>
            </Link>
            <Link href={pdfHref} target="_blank">
                <button className="btn bg-primary hover:bg-primary/80 w-fit text-black rounded-lg font-medium">
                    <DownloadIcon size={18} />
                    Descargar Catalogo
                </button>
            </Link>
        </div>
    );
}
