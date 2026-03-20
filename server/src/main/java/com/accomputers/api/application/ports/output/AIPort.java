package com.accomputers.api.application.ports.output;

import com.accomputers.api.application.dtos.ChatMessageDto;
import com.accomputers.api.application.dtos.SalesChatResponse;

import java.util.List;

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
     * Multi-turn chat with the sales assistant (model + tools configured in infrastructure).
     *
     * @param messages conversation history and current user turn; must not be null or empty
     * @return assistant reply and optional products consulted via tools during the call
     */
    SalesChatResponse chat(List<ChatMessageDto> messages);

    /**
     * One-shot summary from structured product facts (no tools). Used for product page overview.
     *
     * @param productContextText facts about one product; must not be null
     */
    String generateProductOverview(String productContextText);
}
