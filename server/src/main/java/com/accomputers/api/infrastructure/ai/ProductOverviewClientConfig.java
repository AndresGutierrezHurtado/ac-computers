package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat client without tools: short product summaries for the storefront.
 */
@Configuration
public class ProductOverviewClientConfig {

    private static final String OVERVIEW_SYSTEM = """
            Eres el redactor de AC Computers. Recibes datos estructurados de un producto (nombre, descripción, precio, condición, marca, subcategoría y especificaciones técnicas).

            Escribe un único bloque en español, tono claro y profesional, tipo "resumen para el comprador":
            - Qué es el producto y para qué sirve (1–2 frases).
            - Lo más relevante de las especificaciones (sin inventar datos que no vengan en el texto).
            - Menciona condición (nuevo/usado/etc.) y rango de precio si aplica.

            No uses saludos ni preguntas al final. Máximo ~180 palabras. Puedes usar viñetas breves si ayuda la lectura.""";

    @Bean
    public ChatClient productOverviewChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(OVERVIEW_SYSTEM)
                .build();
    }
}
