package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.dtos.ChatMessageDto;
import com.accomputers.api.application.dtos.SalesChatResponse;
import com.accomputers.api.application.ports.output.AIPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Flux;

@Component
public class AIPortImpl implements AIPort {

    private final SpringAiEmbeddingSupport embeddingSupport;
    private final ChatClient salesChatClient;
    private final ChatClient productOverviewChatClient;
    private final SalesChatToolTraceHolder traceHolder;

    public AIPortImpl(
            SpringAiEmbeddingSupport embeddingSupport,
            @Qualifier("salesChatClient") ChatClient salesChatClient,
            @Qualifier("productOverviewChatClient") ChatClient productOverviewChatClient,
            SalesChatToolTraceHolder traceHolder) {
        this.embeddingSupport = embeddingSupport;
        this.salesChatClient = salesChatClient;
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
    public SalesChatResponse chat(List<ChatMessageDto> messages) {
        traceHolder.clear();
        try {
            List<Message> springMessages = new ArrayList<>(messages.size());
            for (ChatMessageDto m : messages) {
                switch (m.role()) {
                    case USER -> springMessages.add(new UserMessage(m.content()));
                    case ASSISTANT -> springMessages.add(new AssistantMessage(m.content()));
                    case SYSTEM -> springMessages.add(new SystemMessage(m.content()));
                }
            }
            String content = salesChatClient.prompt()
                    .messages(springMessages)
                    .call()
                    .content();
            return new SalesChatResponse(content, traceHolder.drain());
        } catch (RuntimeException e) {
            traceHolder.drain();
            throw e;
        }
    }

    @Override
    public Flux<String> generateProductOverview(String productContextText) {
        return productOverviewChatClient.prompt()
                .user(productContextText)
                .stream()
                .content();
    }
}
