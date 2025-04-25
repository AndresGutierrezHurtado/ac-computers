export const feedbackTemplate = (email, subject, message) => {
    return `
        <!DOCTYPE html>
        <html>
            <head>
                <title>Email Confirmation</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f0f0f0; color: #333; padding-top: 2rem; padding-bottom: 2rem;">
                <div style="max-width: 500px; margin: 0 auto; background-color: #ffffff; padding: 20px; border: 1px solid #ddd; text-align: center;">
                    <h1 style="font-size: 24px; color: #000; margin-bottom: 20px; font-weight: 700;">
                        Formulario Contacto 
                        <span style="font-size: 28px; color: #007bff; font-weight: 800;">AC Computers</span>
                    </h1>

                    <p style="font-size: 16px; margin: 10px 0; color: #666;">Email de contacto: <strong>${email}</strong></p>
                    <p style="font-size: 16px; margin: 10px 0; color: #666;">Asunto: <strong>${subject}</strong></p>
                    <p style="font-size: 16px; margin: 10px 0; color: #666;">Mensaje:</p>
                    <p style="font-size: 16px; margin: 10px 0; color: #333;">${message}</p>
                    
                    <a href="mailto:${email}" style="display: inline-block; background-color: #007bff; color: #ffffff; padding: 12px 25px; text-decoration: none; border-radius: 4px; font-size: 16px; margin: 20px 0;">
                        Responder Correo
                    </a>

                    <h2 style="font-size: 20px; margin-top: 20px; color: #007bff;">¿Algún Error?</h2>
                    <p style="font-size: 14px; color: #666;">
                        Envía la información del error al siguiente correo:
                        <a href="mailto:andres52885241@gmail.com" style="color: #007bff; text-decoration: none;">andres52885241@gmail.com</a>
                    </p>
                    <p style="font-size: 12px; color: #999;">© Andrés Gutiérrez Hurtado</p>
                </div>
            </body>
        </html>
    `;
};

export const recoverAccount = (token, name) => {
    return `
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8" />
            <title>Recuperación de cuenta</title>
        </head>
        <body style="margin: 0; padding: 2rem 0; font-family: Arial, sans-serif; background-color: #111722; color: #f8fafc;">
            <div style="max-width: 500px; margin: 0 auto; background-color: #1e293b; padding: 20px; border: 1px solid #334155; text-align: center; border-radius: 8px;">
            
            <span style="font-size: 28px; color: #4e99d3; font-weight: 800; display: block; margin-bottom: 10px;">
                AC Computers
            </span>

            <h1 style="font-size: 22px; color: #f8fafc; margin: 0 0 20px 0; font-weight: 700;">
                Recuperación de cuenta ${name}
            </h1>

            <p style="font-size: 16px; color: #f8fafc; margin: 10px 0;">
                Haz clic en el siguiente botón para recuperar tu cuenta:
            </p>

            <a href="${process.env.NEXTAUTH_URL}/reset/${token}"
                style="display: inline-block; background-color: #4e99d3; color: #020617; padding: 12px 25px; text-decoration: none; border-radius: 4px; font-size: 16px; margin: 20px 0;">
                Recuperar cuenta
            </a>

            <h2 style="font-size: 18px; margin-top: 30px; color: #4e99d3;">¿Algún error?</h2>
            <p style="font-size: 14px; color: #f8fafc; margin: 8px 0;">
                Envía la información del error al siguiente correo:
                <a href="mailto:andres52885241@gmail.com" style="color: #4e99d3; text-decoration: none;">andres52885241@gmail.com</a>
            </p>

            <p style="font-size: 12px; color: #999; margin-top: 30px;">© Andrés Gutiérrez Hurtado</p>
            </div>
        </body>
        </html>
    `;
};
