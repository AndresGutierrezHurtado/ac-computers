import { NextResponse } from "next/server";
import { Resend } from "resend";

// Database
import { Recovery, User } from "@/database/models";

// Hooks
import { recoverAccount } from "@/hooks/emailTemplates";
import { Op } from "sequelize";

export async function POST(request) {
    const transaction = await Recovery.sequelize.transaction();
    try {
        const { user_email } = await request.json();

        const user = await User.findOne({
            where: { user_email },
        });

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

        const [recovery, wasCreated] = await Recovery.findOrCreate({
            where: {
                user_id: user.user_id,
                recovery_expiration: { [Op.gt]: new Date(Date.now() - 300000) },
                recovery_state: "active",
            },
            defaults: {
                user_id: user.user_id,
                recovery_expiration: new Date(Date.now() + 300000), // 5 minutes
            },
        });

        if (!wasCreated) {
            await Recovery.update(
                { recovery_expiration: new Date(Date.now() + 300000) },
                { where: { recovery_id: recovery.recovery_id } }
            );
        }

        const resend = new Resend(process.env.EMAIL_API);

        await resend.emails.send({
            from: "AC Computers <onboarding@resend.dev>",
            to: "andres52885241@gmail.com",
            subject: `Recupera tu contraseña | AC Computers`,
            html: recoverAccount(recovery.recovery_id, user.user_name),
        });

        await transaction.commit();
        return NextResponse.json(
            {
                success: true,
                message: "Correo enviado correctamente",
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
                message: "Hubo un error al enviar el correo: " + error.message,
                data: null,
            },
            { status: 500 }
        );
    }
}
