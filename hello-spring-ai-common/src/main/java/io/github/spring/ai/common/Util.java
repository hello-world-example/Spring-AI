package io.github.spring.ai.common;

import io.github.spring.ai.common.embedding.AllMiniLmL6v2EmbeddingMain;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;

public class Util {

    public static String srcMainResources() {
        return AllMiniLmL6v2EmbeddingMain.class.getResource("/").getPath().replace("target/classes", "src/main/resources");
    }

    /**
     * dims = 384
     */
    public static EmbeddingModel newEmbeddingModel() throws Exception {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setResourceCacheDirectory(Util.srcMainResources() + "/spring-ai-onnx-model");
        embeddingModel.afterPropertiesSet();
        return embeddingModel;
    }

}
