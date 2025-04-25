import { NextResponse } from "next/server";
import crypto from "crypto";

// Database
import { Product, Category, Spec, Multimedia } from "@/database/models";
import { Op } from "sequelize";

// Hooks
import { uploadFile } from "@/hooks/useUploadImage";

export async function GET(resquest) {
    try {
        const { searchParams } = new URL(resquest.url);

        const type = parseInt(searchParams.get("type"));
        const search = searchParams.get("search") || "";
        const page = parseInt(searchParams.get("page")) || 1;
        const limit = parseInt(searchParams.get("limit")) || 10;
        const offset = (page - 1) * limit;

        const { rows: products, count } = await Product.findAndCountAll({
            where: {
                [Op.and]: [
                    { category_id: type || { [Op.in]: [1, 2] } },
                    {
                        [Op.or]: [
                            { product_name: { [Op.iLike]: `%${search}%` } },
                            { product_description: { [Op.iLike]: `%${search}%` } },
                        ],
                    },
                ],
            },
            limit,
            offset,
            include: [
                {
                    model: Category,
                    as: "category",
                },
            ],
        });

        return NextResponse.json({
            success: true,
            message: "Productos obtenidos correctamente",
            count,
            limit,
            page,
            data: products,
        });
    } catch (error) {
        return NextResponse.json({
            success: false,
            message: `Error al obtener productos: ${error.message}`,
            data: error,
        });
    }
}

export async function POST(request) {
    const t = await Product.sequelize.transaction();

    try {
        const { product, product_image, specs, multimedias } = await request.json();
        product.product_id = crypto.randomUUID();

        if (product_image) {
            const { data: url } = await uploadFile(product_image, product.product_id, "/");

            product.product_image_url = url;
        }

        const productResult = await Product.create(product, { transaction: t });

        const specsResult = await Spec.bulkCreate(
            specs.map((spec) => ({
                ...spec,
                product_id: product.product_id,
            })),
            { transaction: t }
        );

        if (multimedias) {
            const multimediaData = await Promise.all(
                multimedias.map(async (file) => {
                    const id = crypto.randomUUID();
                    const { data: url } = await uploadFile(file, id, "/medias");

                    return {
                        media_id: id,
                        media_url: url,
                        product_id: product.product_id,
                    };
                })
            );

            await Multimedia.bulkCreate(multimediaData);
        }

        await t.commit();
        return NextResponse.json({
            success: true,
            message: "Producto creado correctamente",
            data: { productResult, specsResult },
        });
    } catch (error) {
        await t.rollback();
        return NextResponse.json({
            success: false,
            message: `Error al crear producto: ${error.message}`,
            data: error,
        });
    }
}
