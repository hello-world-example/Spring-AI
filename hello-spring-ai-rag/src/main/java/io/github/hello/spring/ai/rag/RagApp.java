package io.github.hello.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.ollama.autoconfigure.OllamaApiAutoConfiguration;
import org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration;
import org.springframework.ai.model.ollama.autoconfigure.OllamaEmbeddingAutoConfiguration;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication(
        exclude = {
                /* Maven: org.springframework.ai:spring-ai-autoconfigure-model-ollama:2.0.0 */
                OllamaApiAutoConfiguration.class,
                OllamaChatAutoConfiguration.class,
                OllamaEmbeddingAutoConfiguration.class,
        }
)
public class RagApp {

    static void main() {
        SpringApplication.run(RagApp.class);
    }

    @Bean
    public static ApplicationRunner runner() {
        return args -> System.out.println("Basic runner works!");
    }


//    @Bean
//    public static CommandLineRunner client(ChatClient.Builder openAiChatModel) {
//        return _ -> {
//            System.out.println("CommandLineRunner");
//            ChatClient chatClient = openAiChatModel.build();
//            String hi = chatClient.prompt("Hi")
//                    .call()
//                    .content();
//            System.out.println(hi);
//        };
//    }
}
