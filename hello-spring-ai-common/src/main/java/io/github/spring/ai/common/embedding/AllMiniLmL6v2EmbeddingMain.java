package io.github.spring.ai.common.embedding;

import org.springframework.ai.transformers.TransformersEmbeddingModel;

import java.util.List;
import java.util.Map;

public class AllMiniLmL6v2EmbeddingMain {


    public static void main(String[] args) throws Exception {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();

        // (optional) defaults to classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json
        embeddingModel.setTokenizerResource("classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json");
        // (optional) defaults to classpath:/onnx/all-MiniLM-L6-v2/model.onnx
        embeddingModel.setModelResource("classpath:/onnx/all-MiniLM-L6-v2/model.onnx");
        // (optional) defaults to ${java.io.tmpdir}/spring-ai-onnx-model
        // Only the http/https resources are cached by default.
        String srcMainResources = AllMiniLmL6v2EmbeddingMain.class.getResource("/").getPath()
                .replace("target/classes", "src/main/resources");
        embeddingModel.setResourceCacheDirectory(srcMainResources + "/spring-ai-onnx-model");

        // (optional) Set the tokenizer padding if you see an errors like:
        // "ai.onnxruntime.OrtException: Supplied array is ragged, ..."
        embeddingModel.setTokenizerOptions(Map.of("padding", "true"));

        embeddingModel.afterPropertiesSet();

        for (int i = 0; i < 10000; i++) {

            List<float[]> embeddings = embeddingModel.embed(List.of("Hello world", "World is big"));

            System.out.println(embeddings.size());
            System.out.println(embeddings.getFirst().length);
            System.out.println(embeddings.getLast().length);
        }

    }

}
