package com.accomputers.api.application.ports.output;

import com.accomputers.api.domain.entities.Product;

import java.util.List;

/**
 * Output port for AI capabilities: embeddings, semantic product search, and grounded chat.
 */
public interface AIPort {

    /**
     * @param text input text to embed; must not be null
     * @return embedding as a list of floats (same order as model output)
     */
    List<Float> embed(String text);

    /**
     * @param context grounded information for the model (must not be null)
     * @param question user question (must not be null)
     * @return assistant reply text
     */
    String ask(String context, String question);
}
