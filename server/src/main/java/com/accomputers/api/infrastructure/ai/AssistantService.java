package com.accomputers.api.infrastructure.ai;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;

@Component
public class AssistantService {

    private final ChatClient chatClient;

    @Autowired
    public AssistantService(ProductTools productTools, CompanyTools companyTools, OllamaChatModel chatModel) {

        OllamaChatOptions options = OllamaChatOptions.builder()
                .model("qwen3:4b")
                .temperature(0.3)
                .build();

        ToolCallback[] toolCallbacks = ToolCallbacks.from(productTools, companyTools);
        List<ToolCallback> toolCallbacksList = Arrays.asList(toolCallbacks);
        options.setToolCallbacks(toolCallbacksList);

        this.chatClient = ChatClient.builder(chatModel)
                .defaultOptions(options)
                .build();
    }

    public String chat(String message) {
        return chatClient.prompt()
                .system("""
Eres un asesor de ventas de AC Computers.
Tu objetivo es recomendar productos disponibles en el inventario o responder preguntas generales sobre la empresa.
Reglas obligatorias:
- Si preguntan algo que no esta relacionado con la empresa o los productos, responde que no podemos ayudarte con eso.
- Solo puedes recomendar productos que recibas explícitamente desde el inventario o del contexto de las tools.
- Si no recibes productos del inventario, NO recomiendes ningún producto.
- Nunca inventes modelos, precios o especificaciones.
- Si no hay productos disponibles, indícalo claramente y sugiere escribir al WhatsApp +57 320 920 2177.
- Responde en español, de forma profesional y clara.
                """)
                .user(message)
                .call()
                .content();
    }
}
