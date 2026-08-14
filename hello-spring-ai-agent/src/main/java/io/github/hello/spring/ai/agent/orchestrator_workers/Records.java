package io.github.hello.spring.ai.agent.orchestrator_workers;

import java.util.List;

public interface Records {
    /**
     * Represents a subtask identified by the orchestrator that needs to be executed
     * by a worker.
     *
     * @param type        The type or category of the task (e.g., "formal",
     *                    "conversational")
     * @param description Detailed description of what the worker should accomplish
     */
    record Task(String type, String description) {
    }

    /**
     * Response from the orchestrator containing task analysis and breakdown into
     * subtasks.
     *
     * @param analysis Detailed explanation of the task and how different approaches
     *                 serve its aspects
     * @param tasks    List of subtasks identified by the orchestrator to be
     *                 executed by workers
     */
    record OrchestratorResponse(String analysis, List<Task> tasks) {
    }

    /**
     * Final response containing the orchestrator's analysis and combined worker
     * outputs.
     *
     * @param analysis        The orchestrator's understanding and breakdown of the
     *                        original task
     * @param workerResponses List of responses from workers, each handling a
     *                        specific subtask
     */
    record FinalResponse(String analysis, List<String> workerResponses) {
    }
}
