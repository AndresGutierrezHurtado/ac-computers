package com.accomputers.api.infrastructure.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AssistantService {

    @SystemMessage("Responde educado y breve")
    public String chat(@UserMessage String userMessage);
}
