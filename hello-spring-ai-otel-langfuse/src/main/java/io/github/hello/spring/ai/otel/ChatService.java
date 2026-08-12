package io.github.hello.spring.ai.otel;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public String testAiCall() {
        log.info("Invoking LLM");
        String answer = chatClient.prompt("Reply with the word 'java'").call().content();
        log.info("AI answered: {}", answer);
        return answer;
    }
}