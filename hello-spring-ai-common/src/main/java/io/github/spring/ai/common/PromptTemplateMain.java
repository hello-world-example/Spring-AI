package io.github.spring.ai.common;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

public class PromptTemplateMain {

    static void main() {

        PromptTemplate template = PromptTemplate.builder()
                .template("Hello {message}")
                .build();

        /* 直接渲染成 String */
        render(template);

        /* Message */
        Message message = template.createMessage(Map.of("message", "world"));
        System.out.println(message);

        /* Prompt 包含 SystemMessage 和 UserMessage */
        Prompt prompt = template.create(Map.of("message", "world"));
        System.out.println(prompt);

    }

    private static void render(PromptTemplate template) {
        String rendered = template.render(Map.of("message", "world"));
        System.out.println(rendered);
    }
}
