package com.accomputers.api.infrastructure;

// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Ports
import com.accomputers.api.application.ports.output.ProductRecommendationInterface;

// AI
import com.accomputers.api.infrastructure.ai.AssistantService;

@Component
public class ProductRecommendationService implements ProductRecommendationInterface {
    private final AssistantService assistantService;

    @Autowired
    public ProductRecommendationService(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Override
    public String getRecommendations(String request) {
        return assistantService.chat(request);
    }
}