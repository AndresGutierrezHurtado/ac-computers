import { NextResponse } from "next/server";
import { Resend } from "resend";

// Database
import { Recovery, User } from "@/database/models";

export async function GET(request, { params }) {
    const { id: recovery_id } = await params;

    try {
        const recovery = await Recovery.findOne({
            where: { recovery_id },
            include: ["user"],
        });

        if (!recovery) {
            return NextResponse.json(
                {
                    success: false,
                    message: "No se encontró la recuperación",
                    data: null,
                },
                { status: 404 }
            );
        }

        if (new Date(recovery.recovery_expiration) < new Date()) {
            return NextResponse.json(
                {
                    success: false,
                    message: "El tiempo de recuperación ha expirado",
                    data: null,
                },
                { status: 401 }
            );
        }

        if (recovery.recovery_state === "used") {
            return NextResponse.json(
                {
                    success: false,
                    message: "La recuperación ya fue utilizada",
                    data: null,
                },
                { status: 401 }
            );
        }

        return NextResponse.json(
            {
                success: true,
                message: "Recuperación encontrada",
                data: recovery,
            },
            { status: 200 }
        );
    } catch (error) {
        console.error(error.message);
        return NextResponse.json(
            {
                success: false,
                message: "Error al recuperar la información",
                data: null,
            },
            { status: 500 }
        );
    }
}

export async function PUT(request, { params }) {
    const transaction = await Recovery.sequelize.transaction();

    try {
        const { id } = params;
        const { user_password } = await request.json();

        const recovery = await Recovery.findOne({
            where: { recovery_id: id },
        });
        const user = await User.findOne({
            where: { user_id: recovery.user_id },
        });

        if (!recovery) {
            return NextResponse.json(
                {
                    success: false,
                    message: "No se encontró la recuperación",
                    data: null,
                },
                { status: 404 }
            );
        }

        if (!user) {
            return NextResponse.json(
                {
                    success: false,
                    message: "No se encontró el usuario",
                    data: null,
                },
                { status: 404 }
            );
        }

        if (recovery.recovery_state === "used") {
            return NextResponse.json(
                {
                    success: false,
                    message: "La recuperación ya fue utilizada",
                    data: null,
                },
                { status: 401 }
            );
        }

        if (recovery.recovery_expiration < new Date()) {
            return NextResponse.json(
                {
                    success: false,
                    message: "El tiempo de recuperación ha expirado",
                    data: null,
                },
                { status: 401 }
            );
        }

        await recovery.update(
            { recovery_state: "used", recovery_expiration: new Date() },
            { where: { recovery_id: id }, transaction }
        );
        await user.update({ user_password }, { where: { user_id: recovery.user_id }, transaction });

        await transaction.commit();
        return NextResponse.json(
            {
                success: true,
                message: "Contraseña actualizada correctamente",
                data: null,
            },
            { status: 200 }
        );
    } catch (error) {
        console.error(error.message);
        await transaction.rollback();
        return NextResponse.json(
            {
                success: false,
                message: "Error al actualizar la contraseña",
                data: null,
            },
            { status: 500 }
        );
    }
}
