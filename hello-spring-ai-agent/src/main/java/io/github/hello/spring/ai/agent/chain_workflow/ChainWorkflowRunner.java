package io.github.hello.spring.ai.agent.chain_workflow;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class ChainWorkflowRunner implements CommandLineRunner {

    private final ChatClient chatClient;

    public ChainWorkflowRunner(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    String report = """
            Q3 Performance Summary:
            Our customer satisfaction score rose to 92 points this quarter.
            Revenue grew by 45% compared to last year.
            Market share is now at 23% in our primary market.
            Customer churn decreased to 5% from 8%.
            New user acquisition cost is $43 per user.
            Product adoption rate increased to 78%.
            Employee satisfaction is at 87 points.
            Operating margin improved to 34%.
            """;


    @Override
    public void run(String... args) {
        new ChainWorkflow(chatClient).chain(report);
    }
}
