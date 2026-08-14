package io.github.hello.spring.ai.agent.orchestrator_workers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;


public class OrchestratorWorkersRunner implements CommandLineRunner {

    private final ChatClient chatClient;

    public OrchestratorWorkersRunner(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public void run(String... args) throws Exception {
        new OrchestratorWorkers(chatClient)
                .process("Write a product description for a new eco-friendly water bottle");
    }
}
