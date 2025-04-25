import { NextResponse } from "next/server";
// import puppeteer from "puppeteer-core";
// import chromium from "@sparticuz/chromium";

import { Product } from "@/database/models";

export async function GET(request) {
    const isDev = process.env.NODE_ENV === "development";
    const puppeteer = isDev ? await import("puppeteer") : await import("puppeteer-core");
    const chromium = isDev ? null : (await import("@sparticuz/chromium")).default;

    const { searchParams } = new URL(request.url);
    const type = parseInt(searchParams.get("type"));

    try {
        const products = await Product.findAll({ include: ["category"] });

        const computers = products.filter((p) => p.category_id === 1);
        const components = products.filter((p) => p.category_id === 2);

        // Generar HTML para el PDF
        const htmlContent = `
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Reporte de Productos</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; }
                    h1 { text-align: center; color: #4e99d3; }
                    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <h1>AC COMPUTERS</h1>
                <p style="text-align: center;">Punto de venta: Centro Comercial Alta Tecnología Cra. 15 No 77-05 Local __ primer piso | Móvil: 311 8835868 | amaliacastro78@gmail.com</p>
                ${
                    type === 1 || !type
                        ? `
                            <h2>Computadores</h2>
                            <table>
                                <tr><th>Producto</th><th>Precio</th></tr>
                                ${computers
                                    .map(
                                        (p) => `
                                <tr>
                                    <td><a href="${process.env.APP_DOMAIN}/product/${
                                            p.product_id
                                        }">${p.product_id.split("-")[1]} - ${
                                            p.product_name
                                        }</a></td>
                                    <td>COP ${parseInt(
                                        p.product_price * (1 - p.product_discount / 100)
                                    ).toLocaleString("es-CO")}</td>
                                </tr>`
                                    )
                                    .join("")}
                            </table>
                        `
                        : ""
                }

                ${
                    type === 2 || !type
                        ? `
                            <h2>Componentes</h2>
                            <table>
                                <tr><th>Producto</th><th>Precio</th></tr>
                                ${components
                                    .map(
                                        (p) => `
                                <tr>
                                    <td><a href="${process.env.APP_DOMAIN}/product/${
                                            p.product_id
                                        }">${p.product_id.split("-")[1]} - ${
                                            p.product_name
                                        }</a></td>
                                    <td>COP ${parseInt(
                                        p.product_price * (1 - p.product_discount / 100)
                                    ).toLocaleString("es-CO")}</td>
                                </tr>`
                                    )
                                    .join("")}
                            </table>`
                        : ""
                }
                </body>
                </html>
                `;

        const browser = await puppeteer.launch({
            args: isDev ? [] : chromium.args,
            executablePath: isDev ? undefined : await chromium.executablePath(),
            headless: isDev ? true : chromium.headless,
        });

        const page = await browser.newPage();

        await page.setContent(htmlContent, { waitUntil: "networkidle0" });
        const pdfBuffer = await page.pdf({ format: "A4", quality: 80 });

        await browser.close();

        return new NextResponse(pdfBuffer, {
            status: 200,
            headers: {
                "Content-Type": "application/pdf",
                "Content-Disposition": `attachment; filename="AC COMPUTERS LISTA PRECIOS ${
                    type === 1 ? " COMPUTADORES" : type === 2 ? " COMPONENTES" : ""
                }.pdf"`,
            },
        });
    } catch (error) {
        return NextResponse.json(
            { success: false, message: "Error al generar el PDF: " + error.message },
            { status: 500 }
        );
    }
}
