export async function downloadCatalogPdf(endpoint = "/pdf/generate") {
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    if (!baseUrl) {
        throw new Error("NEXT_PUBLIC_API_URL is not set");
    }

    const response = await fetch(`${baseUrl}${endpoint}`, {
        method: "POST",
    });

    if (!response.ok) {
        throw new Error("Failed to generate catalog");
    }

    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "catalogo-ac-computers.pdf";
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);
}
