package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Component;

@Component
public class AssistantService {
    private final OllamaChatModel chatModel;

    public AssistantService() {
        OllamaApi ollamaApi = OllamaApi.builder().build();

        this.chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .model("llama3.1:8b")
                                .temperature(0.4)
                                .build())
                .build();
    }

    public String chat(String message) {
        return this.chatModel.call(message);
    }
}
