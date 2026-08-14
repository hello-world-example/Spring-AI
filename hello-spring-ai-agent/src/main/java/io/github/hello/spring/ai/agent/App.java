package io.github.hello.spring.ai.agent;

import io.github.hello.spring.ai.agent.chain_workflow.ChainWorkflowRunner;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner chainWorkflowRunner(ChatClient.Builder builder) {
        return new ChainWorkflowRunner(builder.clone());
    }

}
