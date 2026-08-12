package io.github.spring.ai.common.embedding;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;

import java.util.List;

public class OllamaEmbeddingMain {

    public static void main(String[] args) {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl("http://localhost:11434")
                .build();

        OllamaEmbeddingOptions ollamaEmbeddingOptions = OllamaEmbeddingOptions.builder()
//                .model("qwen3-embedding:8b") // 4096
                .model("qwen3-embedding:0.6b") // 1024
                .build();

        OllamaEmbeddingModel ollamaEmbeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .options(ollamaEmbeddingOptions)
                .build();

        for (int i = 0; i < 100; i++) {

            List<float[]> embeddings = ollamaEmbeddingModel.embed(List.of("Hello world", "World is big"));

            System.out.println(embeddings.size());
            System.out.println(embeddings.getFirst().length);
            System.out.println(embeddings.getLast().length);
        }
    }

}
