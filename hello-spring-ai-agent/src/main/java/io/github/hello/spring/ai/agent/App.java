package io.github.hello.spring.ai.agent;

import io.github.hello.spring.ai.agent.chain_workflow.ChainWorkflowRunner;
import io.github.hello.spring.ai.agent.orchestrator_workers.OrchestratorWorkersRunner;
import io.github.hello.spring.ai.agent.parallelization_workflow.ParallelizationlWorkflowRunner;
import io.github.hello.spring.ai.agent.routing_workflow.RoutingWorkflowRunner;
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
//    @Bean
//    public CommandLineRunner parallelizationlWorkflowRunner(ChatClient.Builder builder) {
//        return new ParallelizationlWorkflowRunner(builder.clone());
//    }

    /**
     * 路由： 先通过LLM选择专家Prompt 》〉》〉 再通过专家Prompt回答问题
     */
//    @Bean
//    public CommandLineRunner routingWorkflowRunner(ChatClient.Builder builder) {
//        return new RoutingWorkflowRunner(builder.clone());
//    }

    /**
     * 编排
     * 1/ 先由 LLM 对任务进行拆分
     * 2/ 再让 LLM 处理拆分后的任务
     */
    @Bean
    public CommandLineRunner orchestratorWorkersRunner(ChatClient.Builder builder) {
        return new OrchestratorWorkersRunner(builder.clone());
    }

}
