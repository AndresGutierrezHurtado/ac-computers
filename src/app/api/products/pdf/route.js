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
            </head>
            <body style="background-color: #111827; color: white; font-family: system-ui; padding: 40px;">
                <div style="text-align: center;">
                    <h1 style="margin: 0; font-size: 32px; font-weight: 800;">
                    <span style="color: white;">AC</span>
                    <span style="font-style: italic; color: #4e99d3;">COMPUTERS</span>
                    </h1>
                    <p style="margin: 5px 0;">Punto de venta: Centro Comercial Alta Tecnología</p>
                    <p style="margin: 3px 0;">Cra. 15 No 77-05 Local __ primer piso</p>
                    <p style="margin: 3px 0;">Móvil: 311 8835868</p>
                    <p style="margin: 3px 0;">amaliacastro78@gmail.com</p>
                </div>
                ${
                    type === 1 || !type
                        ? `
                            <h2>Computadores</h2>
                            <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
                                <tr style="background-color: #4e99d3; color: #020617; border-radius: 5px;">
                                    <th style="text-align: left;">Producto</th>
                                    <th style="text-align: left;">Precio</th>
                                </tr>
                                ${computers
                                    .map(
                                        (p, idx) => `
                                    <tr>
                                        <td style="width: 80%; padding-top: ${
                                            idx == 0 ? "10px" : "0"
                                        }"><a style="color: #4e99d3;" href="${
                                            process.env.APP_DOMAIN
                                        }/product/${p.product_id}">${
                                            p.product_id.split("-")[1]
                                        } - ${p.product_name}</a></td>
                                        <td style="width: 20%; padding-top: ${idx == 0 ? "10px" : "0"}">COP ${parseInt(
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
                            <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
                                <tr style="background-color: #4e99d3; color: #020617; border-radius: 5px;">
                                    <th style="text-align: left;">Producto</th>
                                    <th style="text-align: left;">Precio</th>
                                </tr>
                                ${components
                                    .map(
                                        (p, idx) => `
                                <tr>
                                    <td style="width: 80%; padding-top: ${
                                        idx == 0 ? "10px" : "0"
                                    }"><a style="color: #4e99d3;" href="${
                                            process.env.APP_DOMAIN
                                        }/product/${p.product_id}">${
                                            p.product_id.split("-")[1]
                                        } - ${p.product_name}</a></td>
                                    <td style="width: 20%; padding-top: ${idx == 0 ? "10px" : "0"}">COP ${parseInt(
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
        const pdfBuffer = await page.pdf({ format: "A4", quality: 80, printBackground: true });

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
