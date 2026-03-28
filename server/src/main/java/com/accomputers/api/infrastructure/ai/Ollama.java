package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.dtos.ChatMessageDto;
import com.accomputers.api.application.ports.output.AIPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
public class Ollama implements AIPort {

    private final SpringAiEmbeddingSupport embeddingSupport;
    private final ChatClient salesChatClient;
    private final ChatClient productOverviewChatClient;
    private final SalesChatToolTraceHolder traceHolder;
    private final ProductVectorSearchTool productVectorSearchTool;

    public Ollama(SpringAiEmbeddingSupport embeddingSupport, @Qualifier("salesChatClient") ChatClient salesChatClient, @Qualifier("productOverviewChatClient") ChatClient productOverviewChatClient, SalesChatToolTraceHolder traceHolder,  ProductVectorSearchTool productVectorSearchTool) {
        this.embeddingSupport = embeddingSupport;
        this.salesChatClient = salesChatClient;
        this.productOverviewChatClient = productOverviewChatClient;
        this.traceHolder = traceHolder;
        this.productVectorSearchTool = productVectorSearchTool;
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
    public Flux<String> chat(List<ChatMessageDto> messages) {
        traceHolder.clear();
        List<Message> springMessages = new ArrayList<>(messages.size());
        for (ChatMessageDto m : messages) {
            switch (m.role()) {
                case USER -> springMessages.add(new UserMessage(m.content()));
                case ASSISTANT -> springMessages.add(new AssistantMessage(m.content()));
                case SYSTEM -> springMessages.add(new SystemMessage(m.content()));
            }
        }

        return salesChatClient.prompt()
                .messages(springMessages)
                .stream()
                .chatResponse()
                .map(response -> {
                    String thinking = response.getResult().getMetadata().get("thinking");
                    String answer = response.getResult().getOutput().getText();
                    List<AssistantMessage.ToolCall> toolCalling = response.getResult().getOutput().getToolCalls();

                    return answer != null ? answer : "";
                });
    }

    @Override
    public Flux<String> generateProductOverview(String productContextText) {
        return productOverviewChatClient.prompt().user(productContextText).stream().content();
    }
}
