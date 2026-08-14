package io.github.hello.spring.ai.agent;

import io.github.hello.spring.ai.agent.chain_workflow.ChainWorkflowRunner;
import io.github.hello.spring.ai.agent.parallelization_workflow.ParallelizationlWorkflowRunner;
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

    /**
     * 串行
     */
//    @Bean
//    public CommandLineRunner chainWorkflowRunner(ChatClient.Builder builder) {
//        return new ChainWorkflowRunner(builder.clone());
//    }

    /**
     * 并行
     */
    @Bean
    public CommandLineRunner parallelizationlWorkflowRunner(ChatClient.Builder builder) {
        return new ParallelizationlWorkflowRunner(builder.clone());
    }

}
