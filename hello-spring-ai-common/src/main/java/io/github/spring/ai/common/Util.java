package io.github.spring.ai.common;

import io.github.spring.ai.common.embedding.AllMiniLmL6v2EmbeddingMain;

public class Util {
    public static String srcMainResources() {
        return AllMiniLmL6v2EmbeddingMain.class.getResource("/").getPath().replace("target/classes", "src/main/resources");
    }
}
