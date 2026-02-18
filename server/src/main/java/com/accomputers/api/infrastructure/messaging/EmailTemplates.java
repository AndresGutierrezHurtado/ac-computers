package com.accomputers.api.infrastructure.messaging;

public class EmailTemplates {
    public static String buildHtmlEmail(String name, String email, String message) {
        String escapedName = escapeHtml(name);
        String escapedEmail = escapeHtml(email);
        String escapedMessage = escapeHtml(message);

        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="es">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Nuevo Contacto - AC Computers</title>
                        </head>
                        <body style="margin: 0; padding: 0; font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background-color: #111722; color: #f8fafc;">
                            <table role="presentation" style="width: 100%%; border-collapse: collapse; background-color: #111722; padding: 40px 20px;">
                                <tr>
                                    <td align="center">
                                        <table role="presentation" style="margin: 20px auto; max-width: 600px; width: 100%%; border-collapse: collapse; background-color: #1e293b; border-radius: 12px; overflow: hidden; border: 1px solid #334155; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.3);">
                                            <!-- Header -->
                                            <tr>
                                                <td style="padding: 40px 30px; text-align: center; border-bottom: 1px solid #334155;">
                                                    <div style="display: inline-block; padding: 6px 16px; background-color: rgba(78, 153, 211, 0.1); border: 1px solid rgba(78, 153, 211, 0.3); border-radius: 9999px; margin-bottom: 20px;">
                                                        <span style="color: #4e99d3; font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px;">Nuevo Mensaje</span>
                                                    </div>
                                                    <h1 style="margin: 0; color: #f8fafc; font-size: 28px; font-weight: 800; letter-spacing: -0.5px;">
                                                        AC <span style="color: #4e99d3; font-style: italic;">COMPUTERS</span>
                                                    </h1>
                                                </td>
                                            </tr>

                                            <!-- Content -->
                                            <tr>
                                                <td style="padding: 40px 30px;">
                                                    <p style="margin: 0 0 30px 0; color: #94a3b8; font-size: 16px; line-height: 1.6; text-align: center;">
                                                        Has recibido una nueva consulta a través del formulario de contacto.
                                                    </p>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse; margin-bottom: 20px;">
                                                        <tr>
                                                            <td style="padding: 16px; background-color: #0f172a; border-left: 4px solid #4e99d3; border-radius: 6px;">
                                                                <strong style="color: #4e99d3; font-size: 12px; text-transform: uppercase; letter-spacing: 1px; display: block; margin-bottom: 6px;">Nombre del Cliente</strong>
                                                                <p style="margin: 0; color: #f8fafc; font-size: 18px; font-weight: 600;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse; margin-bottom: 20px;">
                                                        <tr>
                                                            <td style="padding: 16px; background-color: #0f172a; border-left: 4px solid #4e99d3; border-radius: 6px;">
                                                                <strong style="color: #4e99d3; font-size: 12px; text-transform: uppercase; letter-spacing: 1px; display: block; margin-bottom: 6px;">Correo Electrónico</strong>
                                                                <p style="margin: 0; color: #f8fafc; font-size: 16px;">
                                                                    <a href="mailto:%s" style="color: #4e99d3; text-decoration: none; font-weight: 500;">%s</a>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse;">
                                                        <tr>
                                                            <td style="padding: 16px; background-color: #0f172a; border-left: 4px solid #4e99d3; border-radius: 6px;">
                                                                <strong style="color: #4e99d3; font-size: 12px; text-transform: uppercase; letter-spacing: 1px; display: block; margin-bottom: 6px;">Contenido del Mensaje</strong>
                                                                <p style="margin: 0; color: #f8fafc; font-size: 15px; line-height: 1.8; white-space: pre-wrap;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>

                                            <!-- Footer -->
                                            <tr>
                                                <td style="padding: 30px; background-color: #0f172a; text-align: center; border-top: 1px solid #334155;">
                                                    <p style="margin: 0; color: #64748b; font-size: 13px;">
                                                        &copy; 2024 AC Computers. Todos los derechos reservados.
                                                    </p>
                                                    <p style="margin: 8px 0 0 0; color: #475569; font-size: 11px;">
                                                        Este es un mensaje automático generado por el sistema.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </body>
                        </html>
                        """,
                escapedName, escapedEmail, escapedEmail, escapedMessage);
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
