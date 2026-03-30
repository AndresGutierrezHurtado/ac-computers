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
    /**
     * Internal pass: only gathers verified catalog snippets for the next assistant (no sales tone, no analysis).
     */
    static final String RAG_CONTEXT_SYSTEM_PROMPT = """
Eres un asistente interno de AC Computers.
Tu tarea:
1. Entender la intención actual del usuario usando TODO el historial.
2. Si hay intención de compra, usar la tool para buscar productos.
3. Generar un bloque compacto que será la ÚNICA fuente del siguiente asistente.
Debes producir SIEMPRE este formato:
INTENCION:
<una frase corta que represente la intención actual>
PRODUCTOS:
- nombre | precio | condición | marca | descripción corta
REGLAS:
- Si el usuario dice "más barato", "mejor", "otra vez", debes usar el contexto previo.
- Si la tool devuelve [], indícalo en PRODUCTOS: "SIN RESULTADOS".
- No saludes, no vendas, no expliques.
- No inventes datos.
- Máximo compacto posible.
            """;

    /**
     * Visible assistant: must not call tools; reformulates only what appears in the injected inventory block.
     */
    static final String SALES_STREAMING_SYSTEM_PROMPT = """
Eres un asistente de ventas.
Recibirás un bloque con:
- INTENCION del cliente
- PRODUCTOS disponibles
Tu tarea:
- Reformular eso en una respuesta clara y útil para el cliente.
REGLAS:
- SOLO usa la información del bloque
- No agregues nada externo
- No inventes
- No hagas análisis complejo
- No hagas muchas preguntas (máximo 1 opcional)
- Si PRODUCTOS indica "SIN RESULTADOS", dilo claramente
Sé claro, breve y ordenado.
            """;

    @Bean
    public ChatClient salesRagContextChatClient(ChatModel chatModel, ProductVectorSearchTool productVectorSearchTool) {
        return ChatClient.builder(chatModel).defaultSystem(RAG_CONTEXT_SYSTEM_PROMPT).defaultTools(productVectorSearchTool).build();
    }

    @Bean
    public ChatClient salesStreamingChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).defaultSystem(SALES_STREAMING_SYSTEM_PROMPT).build();
    }
}
