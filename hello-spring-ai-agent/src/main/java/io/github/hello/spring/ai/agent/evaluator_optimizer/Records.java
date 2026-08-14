package io.github.hello.spring.ai.agent.evaluator_optimizer;

import java.util.List;

public interface Records {
    /**
     * Represents a solution generation step. Contains the model's thoughts and the
     * proposed solution.
     *
     * @param thoughts The model's understanding of the task and feedback
     * @param response The model's proposed solution
     */
    record Generation(String thoughts, String response) {
    }

    /**
     * Represents an evaluation response. Contains the evaluation result and
     * detailed feedback.
     *
     * @param evaluation The evaluation result (PASS, NEEDS_IMPROVEMENT, or FAIL)
     * @param feedback   Detailed feedback for improvement
     */
    record EvaluationResponse(Evaluation evaluation, String feedback) {

        public enum Evaluation {
            PASS, NEEDS_IMPROVEMENT, FAIL
        }
    }

    /**
     * Represents the final refined response. Contains the final solution and the
     * chain of thought showing the evolution of the solution.
     *
     * @param solution       The final solution
     * @param chainOfThought The chain of thought showing the evolution of the
     *                       solution
     */
    record RefinedResponse(String solution, List<Generation> chainOfThought) {
    }
}
