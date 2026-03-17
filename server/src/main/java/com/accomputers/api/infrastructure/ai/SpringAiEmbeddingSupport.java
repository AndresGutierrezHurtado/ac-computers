package com.accomputers.api.infrastructure.ai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Text to embedding / pgvector literal via Spring AI (Ollama).
 */
@Component
public class SpringAiEmbeddingSupport {

    private final EmbeddingModel embeddingModel;

    public SpringAiEmbeddingSupport(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embedToFloatArray(String text) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("text must not be blank");
        }
        return embeddingModel.embed(text.trim());
    }

    public static String toPgVectorLiteral(float[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("embedding must not be null or empty");
        }
        StringBuilder sb = new StringBuilder(values.length * 12);
        sb.append('[');
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(values[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public String embedToPgVectorLiteral(String text) {
        return toPgVectorLiteral(embedToFloatArray(text));
    }
}
