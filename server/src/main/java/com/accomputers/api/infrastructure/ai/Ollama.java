package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.dtos.ChatMessageDto;
import com.accomputers.api.application.dtos.ProductSearchHitDto;
import com.accomputers.api.application.dtos.SalesChatResponse;
import com.accomputers.api.application.ports.output.AIPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
public class Ollama implements AIPort {

    private final SpringAiEmbeddingSupport embeddingSupport;
    private final ChatClient salesRagContextChatClient;
    private final ChatClient salesStreamingChatClient;
    private final ChatClient productOverviewChatClient;
    private final SalesChatToolTraceHolder traceHolder;

    public Ollama(
            SpringAiEmbeddingSupport embeddingSupport,
            @Qualifier("salesRagContextChatClient") ChatClient salesRagContextChatClient,
            @Qualifier("salesStreamingChatClient") ChatClient salesStreamingChatClient,
            @Qualifier("productOverviewChatClient") ChatClient productOverviewChatClient,
            SalesChatToolTraceHolder traceHolder) {
        this.embeddingSupport = embeddingSupport;
        this.salesRagContextChatClient = salesRagContextChatClient;
        this.salesStreamingChatClient = salesStreamingChatClient;
        this.productOverviewChatClient = productOverviewChatClient;
        this.traceHolder = traceHolder;
    }

    @Override
    public List<Float> embed(String text) {
        float[] raw = embeddingSupport.embedToFloatArray(text);
        List<Float> out = new ArrayList<>(raw.length);
        for (float v : raw) {
            out.add(v);
        }
        return out;
    }

    @Override
    public Flux<SalesChatResponse> chat(List<ChatMessageDto> messages) {
        traceHolder.clear();

        // MAP MESSAGES TO PASS THROUGH SPRING AI
        List<Message> springMessages = new ArrayList<>(messages.size());
        for (ChatMessageDto m : messages) {
            switch (m.role()) {
                case USER -> springMessages.add(new UserMessage(m.content()));
                case ASSISTANT -> springMessages.add(new AssistantMessage(m.content()));
                case SYSTEM -> springMessages.add(new SystemMessage(m.content()));
            }
        }

        // GET PRODUCTS/CONTEXT FROM RAG
        String context = salesRagContextChatClient.prompt()
                .messages(springMessages)
                .call()
                .content();
        List<ProductSearchHitDto> consulted = new ArrayList<>(traceHolder.drain());

        System.out.println("context: " + context);
        System.out.println("consulted: " + consulted);

        String block = StringUtils.hasText(context)
                ? context
                : "(Sin texto de contexto del inventario para esta consulta.)";

        List<Message> salesMessages = new ArrayList<>(springMessages.size() + 1);

        // ADD CONTEXT TO MESSAGES
        salesMessages.add(new SystemMessage("Usa únicamente esta información del inventario como fuente:\n\n" + block));
        salesMessages.addAll(springMessages);

        // STREAM RESPONSE
        return salesStreamingChatClient
                .prompt()
                .messages(salesMessages)
                .stream()
                .chatResponse()
                .map(response -> {
                    String answer = response.getResult().getOutput().getText();
                    if (answer == null) {
                        answer = "";
                    }

                    return new SalesChatResponse(answer, List.copyOf(consulted));
                });
    }

    @Override
    public Flux<String> generateProductOverview(String productContextText) {
        return productOverviewChatClient.prompt().user(productContextText).stream().content();
    }
}
