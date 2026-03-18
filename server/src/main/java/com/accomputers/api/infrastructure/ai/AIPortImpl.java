package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.ports.output.AIPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AIPortImpl implements AIPort {

    private final SpringAiEmbeddingSupport embeddingSupport;

    public AIPortImpl(SpringAiEmbeddingSupport embeddingSupport) {
        this.embeddingSupport = embeddingSupport;
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
    public String ask(String context, String question) {
        return "";
    }
}
