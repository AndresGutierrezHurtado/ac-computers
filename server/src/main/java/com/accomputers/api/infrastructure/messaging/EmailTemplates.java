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
                            <title>Nuevo Feedback - AC Computers</title>
                        </head>
                        <body style="margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color: #f4f4f4;">
                            <table role="presentation" style="width: 100%%; border-collapse: collapse; background-color: #f4f4f4; padding: 20px;">
                                <tr>
                                    <td align="center">
                                        <table role="presentation" style="margin: 20px; max-width: 600px; width: 100%%; border-collapse: collapse; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                            <!-- Header -->
                                            <tr>
                                                <td style="background-color: #764ba2; padding: 30px 20px; text-align: center;">
                                                    <h1 style="margin: 0; color: #ffffff; font-size: 24px; font-weight: 600;">Nuevo Mensaje de Contacto</h1>
                                                    <p style="margin: 10px 0 0 0; color: #ffffff; font-size: 14px; opacity: 0.9;">AC Computers</p>
                                                </td>
                                            </tr>

                                            <!-- Content -->
                                            <tr>
                                                <td style="padding: 30px 20px;">
                                                    <p style="margin: 0 0 20px 0; color: #333333; font-size: 16px; line-height: 1.6;">
                                                        Has recibido un nuevo mensaje de contacto:
                                                    </p>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse; margin: 20px 0;">
                                                        <tr>
                                                            <td style="padding: 12px; background-color: #f8f9fa; border-left: 4px solid #764ba2; border-radius: 4px;">
                                                                <strong style="color: #764ba2; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Nombre:</strong>
                                                                <p style="margin: 8px 0 0 0; color: #333333; font-size: 16px; font-weight: 500;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse; margin: 15px 0;">
                                                        <tr>
                                                            <td style="padding: 12px; background-color: #f8f9fa; border-left: 4px solid #764ba2; border-radius: 4px;">
                                                                <strong style="color: #764ba2; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Email:</strong>
                                                                <p style="margin: 8px 0 0 0; color: #333333; font-size: 16px;">
                                                                    <a href="mailto:%s" style="color: #764ba2; text-decoration: none;">%s</a>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                    </table>

                                                    <table role="presentation" style="width: 100%%; border-collapse: collapse; margin: 15px 0;">
                                                        <tr>
                                                            <td style="padding: 12px; background-color: #f8f9fa; border-left: 4px solid #764ba2; border-radius: 4px;">
                                                                <strong style="color: #764ba2; font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;">Mensaje:</strong>
                                                                <p style="margin: 8px 0 0 0; color: #333333; font-size: 16px; line-height: 1.6; white-space: pre-wrap;">%s</p>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>

                                            <!-- Footer -->
                                            <tr>
                                                <td style="padding: 20px; background-color: #f8f9fa; text-align: center; border-top: 1px solid #e9ecef;">
                                                    <p style="margin: 0; color: #6c757d; font-size: 12px;">
                                                        Este es un mensaje automático del sistema de contacto de AC Computers.
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
