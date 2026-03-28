package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chat client without tools: short product summaries for the storefront.
 */
@Configuration
public class ProductOverviewClientConfig {

    private static final String OVERVIEW_SYSTEM = "Redacta una reseña breve en español, tono profesional y orientada a la decisión de compra. Explica qué es el producto y su nivel (básico, medio o alto), interpreta su utilidad en escenarios reales (trabajo, estudio, gaming, etc.), y resalta ventajas y posibles limitaciones. Menciona condición y precio si están disponibles. No repitas ni enumeres especificaciones técnicas ni copies datos literalmente; conviértelos en beneficios. No hacer follow up questions. Máximo 80-100 palabras.";

    @Bean
    public ChatClient productOverviewChatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(OVERVIEW_SYSTEM)
                .defaultOptions(OllamaChatOptions.builder().model("qwen3:1.7b").build())
                .build();
    }
}
