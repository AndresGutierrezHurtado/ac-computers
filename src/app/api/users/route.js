import { NextResponse } from "next/server";

import { User } from "@/database/models";
import { Op, Sequelize } from "sequelize";

export async function GET(request) {
    try {
        const { searchParams } = new URL(request.url);

        const page = parseInt(searchParams.get("page")) || 1;
        const limit = parseInt(searchParams.get("limit")) || 10;
        const sortQuery = searchParams.get("sort") || "user_id:asc";
        const sort = sortQuery.split(":");
        const search = searchParams.get("search") || "";
        const offset = (page - 1) * limit;

        const { rows: users, count } = await User.findAndCountAll({
            limit,
            offset,
            include: ["role"],
            sort: [sort],
            attributes: {
                exclude: ["user_password"],
            },
            where: {
                [Op.or]: [
                    { user_name: { [Op.iLike]: `%${search}%` } },
                    { user_email: { [Op.iLike]: `%${search}%` } },
                ],
            },
        });

        return NextResponse.json({
            success: true,
            message: "Usuarios obtenidos correctamente",
            count,
            limit,
            page,
            data: users,
        });
    } catch (error) {
        console.error(error);
        return NextResponse.json(
            {
                success: false,
                message: `Error al crear usuario: ${error.message}`,
                data: error,
            },
            { status: 500 }
        );
    }
}

export async function POST(request) {
    try {
        const { user: userJSON } = await request.json();

        const user = await User.create(userJSON);

        return NextResponse.json({
            success: true,
            message: "Usuario creado correctamente",
            data: user,
        });
    } catch (error) {
        if (error.name === "SequelizeUniqueConstraintError") {
            return NextResponse.json(
                {
                    success: false,
                    message: "El correo electrónico ya está en uso",
                    data: error,
                },
                { status: 400 }
            );
        }

        return NextResponse.json(
            {
                success: false,
                message: `Error al crear usuario: ${error.message}`,
                data: error,
            },
            { status: 500 }
        );
    }
}
