package io.github.demo.ai.agents.embedding.onnx;

import org.springframework.ai.transformers.TransformersEmbeddingModel;

import java.util.List;
import java.util.Map;

public class E5SmallV2EmbeddingMain {


    public static void main(String[] args) throws Exception {

        String modelName = "intfloat/e5-small-v2";

        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();

        // (optional) defaults to classpath:/onnx/all-MiniLM-L6-v2/tokenizer.json
        embeddingModel.setTokenizerResource("https://huggingface.co/" + modelName + "/raw/main/tokenizer.json");
        // (optional) defaults to classpath:/onnx/all-MiniLM-L6-v2/model.onnx
        embeddingModel.setModelResource("https://huggingface.co/" + modelName + "/resolve/main/model.onnx");
        embeddingModel.setTokenizerOptions(Map.of("padding", "true"));
        // (optional) defaults to ${java.io.tmpdir}/spring-ai-onnx-model
        // Only the http/https resources are cached by default.
        String srcMainResources = E5SmallV2EmbeddingMain.class.getResource("/").getPath()
                .replace("target/classes", "src/main/resources");
        embeddingModel.setResourceCacheDirectory(srcMainResources + "/spring-ai-onnx-model/" + modelName);

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
