
package io.github.hello.spring.ai.agent.parallelization_workflow;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

public class ParallelizationlWorkflowRunner implements CommandLineRunner {

    private final ChatClient chatClient;

    public ParallelizationlWorkflowRunner(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> parallelResponse = new ParallelizationlWorkflow(chatClient)
                .parallel("""
                                Analyze how market changes will impact this stakeholder group.
                                Provide specific impacts and recommended actions.
                                Format with clear sections and priorities.
                                """,
                        List.of(
                                """
                                        Customers:
                                        - Price sensitive
                                        - Want better tech
                                        - Environmental concerns
                                        """,

                                """
                                        Employees:
                                        - Job security worries
                                        - Need new skills
                                        - Want clear direction
                                        """,

                                """
                                        Investors:
                                        - Expect growth
                                        - Want cost control
                                        - Risk concerns
                                        """,

                                """
                                        Suppliers:
                                        - Capacity constraints
                                        - Price pressures
                                        - Tech transitions
                                        """),
                        4);

        System.out.println(parallelResponse);
    }
}
