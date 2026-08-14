package io.github.hello.spring.ai.agent;

import io.github.hello.spring.ai.agent.evaluator_optimizer.EvaluatorOptimizerRunner;
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
//    @Bean
//    public CommandLineRunner orchestratorWorkersRunner(ChatClient.Builder builder) {
//        return new OrchestratorWorkersRunner(builder.clone());
//    }

    /**
     * 循环评估
     * 1/ 先 LLM 生成
     * 2/ 再 LLM 评估
     * 3/ 评估通过结束，评估不通过 loop 到 第一步继续
     */
    @Bean
    public CommandLineRunner evaluatorOptimizerRunner(ChatClient.Builder builder) {
        return new EvaluatorOptimizerRunner(builder.clone());
    }

}
