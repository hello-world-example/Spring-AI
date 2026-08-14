package io.github.hello.spring.ai.rag.advance;

import org.jspecify.annotations.NonNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class VectorStoreService {

    private final EmbeddingModel embeddingModel;

    private final ResourceLoader resourceLoader;

    public VectorStoreService(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) {
        this.embeddingModel = embeddingModel;
        this.resourceLoader = resourceLoader;
    }

    /**
     * 初始化内存中的向量数据
     */
    public @NonNull SimpleVectorStore initVectorStore() throws IOException {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel)
                //.observationRegistry(null)
                .build();

        // SimpleVectorStore 处理后的数据文件，是个 json
        File dbFile = new File(this.getClass().getResource("/simple/").getFile() + "SimpleVectorStore.db");
        if (dbFile.exists()) {
            // 直接从 db 数据文件加载
            vectorStore.load(dbFile);
            return vectorStore;
        }

        // 原始文件
        Resource sourceResource = resourceLoader.getResource("classpath:/simple/Who-is-Kai.md");
        vectorStore.add(List.of(new Document(
                sourceResource.getContentAsString(StandardCharsets.UTF_8),
                Map.of("filename", Objects.requireNonNull(sourceResource.getFilename()))
        )));
        vectorStore.save(dbFile);

        return vectorStore;
    }

}
