package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class CompanyTools {

    @Tool(description = "Obtiene información general e historia de AC Computers")
    public String getCompanyHistory() {
        return """
            AC Computers es una empresa dedicada a la venta de computadores, accesorios y soluciones tecnológicas.
            Nuestro objetivo es ofrecer equipos confiables y asesoría personalizada según las necesidades del cliente.
        """;
    }

    @Tool(description = "Lista los productos que vende AC Computers")
    public String getProductsInfo() {
        return """
            AC Computers vende computadores de escritorio, computadores portátiles, accesorios y periféricos.
            Todos los productos dependen de disponibilidad en inventario.
        """;
    }

    @Tool(description = "Describe los servicios que ofrece AC Computers")
    public String getServicesInfo() {
        return """
            AC Computers ofrece servicios de mantenimiento preventivo y correctivo de computadores,
            así como asesoría técnica para la compra de equipos.
        """;
    }

    @Tool(description = "Proporciona información de contacto de AC Computers")
    public String getContactInfo() {
        return """
            Puedes comunicarte con AC Computers a través de WhatsApp al +57 320 920 2177.
        """;
    }
}
