package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat client for the sales assistant: system instructions + catalog vector search tool.
 */
@Configuration
public class SalesChatClientConfig {

    static final String SALES_SYSTEM_PROMPT = """
            Eres un asesor de ventas de AC Computers, tienda de portátiles, componentes de PC y periféricos.
            Hablas en español, con tono profesional, cercano y claro.
            Ayuda al cliente a elegir qué comprar: haz preguntas sobre uso (oficina, gaming, estudio, presupuesto),
            prioridades y restricciones.
            No inventes precios ni stock: cuando necesites opciones concretas del inventario, usa la herramienta
            de búsqueda de productos. Si la herramienta devuelve una lista vacía, dilo con honestidad y ofrece
            reformular la búsqueda o ampliar criterios.
            Cierra con una recomendación razonada y, si aplica, invita a revisar el catálogo o a seguir preguntando.
            """;

    @Bean
    public ChatClient salesChatClient(ChatModel chatModel, ProductVectorSearchTool productVectorSearchTool) {
        return ChatClient.builder(chatModel)
                .defaultSystem(SALES_SYSTEM_PROMPT)
                .defaultTools(productVectorSearchTool)
                .build();
    }
}
