package com.accomputers.api.application.ports.output;

import com.accomputers.api.application.dtos.ChatMessageDto;
import com.accomputers.api.application.dtos.SalesChatResponse;
import java.util.List;
import reactor.core.publisher.Flux;

/**
 * Output port for AI: embeddings and sales chat (see infrastructure for models/tools).
 */
public interface AIPort {

    /**
     * @param text input text to embed; must not be null
     * @return embedding as a list of floats (same order as model output)
     */
    List<Float> embed(String text);

    /**
     * Sales chat: a tool-capable pass builds inventory context (RAG), then a streaming model answers using only that context.
     *
     * @param messages conversation history and current user turn; must not be null or empty
     * @return one streamed chunk per {@link SalesChatResponse#message()}; {@link SalesChatResponse#consultedProducts()} repeats the same list
     */
    Flux<SalesChatResponse> chat(List<ChatMessageDto> messages);

    /**
     * One-shot summary from structured product facts (no tools). Used for product page overview.
     *
     * @param productContextText facts about one product; must not be null
     */
    Flux<String> generateProductOverview(String productContextText);
}
